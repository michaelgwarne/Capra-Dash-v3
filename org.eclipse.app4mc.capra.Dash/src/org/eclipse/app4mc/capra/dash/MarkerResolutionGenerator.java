package org.eclipse.app4mc.capra.dash;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

/**
 * 
 * @author Michael Warne
 *
 */

public class MarkerResolutionGenerator implements IMarkerResolutionGenerator {

	@Override
	public IMarkerResolution[] getResolutions(IMarker marker) {
		try {
			String problem = (String) marker.getAttribute("IssueType");
			if(problem.equals("Rename")){
				return new IMarkerResolution[] {
						new DummyNameOnlyQuickFix("Update only Name in the wrapper model."),
						new RenameOrMoveQuickFix("Update Name and URI in the wrapper model."),
						new DummyURIOnlyQuickFix("Update URI in the wrapper model."),
				};}
			if(problem.equals("Move")){
				return new IMarkerResolution[] {
						new RenameOrMoveQuickFix("Update wrapper URI in the model."),

				};}
			if(problem.equals("Delete")){
				return new IMarkerResolution[] {
						new DeleteQuickFix("Set the wrapper properties to NULL."),

				};}
			else return null;

		}
		catch (CoreException e) {
			return new IMarkerResolution[0];
		}
	}

}
