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
package org.cloudfoundry.ide.eclipse.internal.server.core;

import org.cloudfoundry.client.lib.domain.CloudApplication.AppState;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.server.core.IModule;

public class StopApplicationInWaitOperation extends AbstractWaitForStateOperation {

	public StopApplicationInWaitOperation(CloudFoundryServer cloudServer) {
		super(cloudServer, "Stopping application");
	}

	@Override
	protected void doOperation(CloudFoundryServerBehaviour behaviour, IModule module, IProgressMonitor progress)
			throws CoreException {
		behaviour.stopModule(new IModule[] { module }, progress);
	}

	@Override
	protected boolean isInState(AppState state) {
		return state.equals(AppState.STOPPED);
	}

}
