/*******************************************************************************
 * Copyright (c) 2012 VMware, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     VMware, Inc. - initial API and implementation
 *******************************************************************************/
package org.cloudfoundry.ide.eclipse.internal.server.core.tunnel;

import java.util.ArrayList;
import java.util.List;

import org.cloudfoundry.ide.eclipse.internal.server.core.CloudFoundryPlugin;
import org.cloudfoundry.ide.eclipse.internal.server.core.CloudFoundryServer;
import org.cloudfoundry.ide.eclipse.internal.server.core.CloudServerUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class ServiceCommandHelper {

	/**
	 * Only returns a list of servers that actually have services. Skips servers
	 * with no services. Returns a new and updated copy of the servers every
	 * time it is called. Changes to the list will not be reflected until
	 * explicitly persisted using a persistence or save API. This is a synchronous operation.
	 * @param monitor
	 * @return
	 * @throws CoreException
	 */
	public List<ExternalToolLaunchCommandsServer> getUpdatedServerServiceCommands(IProgressMonitor monitor) throws CoreException {
		List<CloudFoundryServer> cfServers = CloudServerUtil.getCloudServers();
		List<ExternalToolLaunchCommandsServer> servicesServer = new ArrayList<ExternalToolLaunchCommandsServer>();

		for (CloudFoundryServer cfServer : cfServers) {
			ExternalToolLaunchCommandsServer server = cfServer.getUpdatedTunnelServiceCommands(monitor);
			if (server != null && server.getServices() != null && !server.getServices().isEmpty()) {
				servicesServer.add(server);
			}
		}

		return servicesServer;
	}
	

	/**
	 * 
	 * This is performed asynchronously.
	 * @param servers
	 * @param monitor
	 * @throws CoreException
	 */
	public void saveServerServiceCommands(final List<ExternalToolLaunchCommandsServer> servers, IProgressMonitor monitor)
			throws CoreException {
		if (servers == null) {
			return;
		}

		Job job = new Job("Saving Server services command definitions") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				for (ExternalToolLaunchCommandsServer server : servers) {
					CloudFoundryServer cloudServer = CloudServerUtil.getCloudServer(server.getServerID());
					if (cloudServer != null) {
						try {
							cloudServer.saveTunnelServiceCommands(server);
						}
						catch (CoreException e) {
							CloudFoundryPlugin.logError(e);
						}
					}
				}
				return Status.OK_STATUS;
			}

		};

		job.setSystem(true);
		job.setPriority(Job.INTERACTIVE);
		job.schedule();

	}

}
