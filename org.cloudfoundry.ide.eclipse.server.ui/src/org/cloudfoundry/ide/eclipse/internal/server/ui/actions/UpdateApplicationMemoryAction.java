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
package org.cloudfoundry.ide.eclipse.internal.server.ui.actions;

import org.cloudfoundry.ide.eclipse.internal.server.core.ApplicationModule;
import org.cloudfoundry.ide.eclipse.internal.server.ui.editor.CloudFoundryApplicationsEditorPage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;



/**
 * @author Terry Denney
 */
public class UpdateApplicationMemoryAction extends CloudFoundryEditorAction {

	private final int memory;

	private final ApplicationModule module;

	public UpdateApplicationMemoryAction(CloudFoundryApplicationsEditorPage editorPage, int memory,
			ApplicationModule module) {
		super(editorPage, RefreshArea.DETAIL);
		this.memory = memory;
		this.module = module;
	}

	@Override
	public String getJobName() {
		return "Updating application memory limit";
	}

	@Override
	public IStatus performAction(IProgressMonitor monitor) throws CoreException {
		getBehavior().updateApplicationMemory(module, memory, monitor);
		return Status.OK_STATUS;
	}

}
