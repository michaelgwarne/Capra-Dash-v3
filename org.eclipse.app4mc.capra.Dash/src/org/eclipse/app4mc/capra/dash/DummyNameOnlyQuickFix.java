package org.eclipse.app4mc.capra.dash;

	import java.io.IOException;
	import java.util.Optional;
	import org.eclipse.app4mc.capra.generic.adapters.TracePersistenceAdapter;
	import org.eclipse.app4mc.capra.generic.artifacts.ArtifactWrapper;
	import org.eclipse.app4mc.capra.generic.artifacts.ArtifactWrapperContainer;
	import org.eclipse.app4mc.capra.generic.artifacts.ArtifactsFactory;
	import org.eclipse.app4mc.capra.generic.helpers.ExtensionPointHelper;
	import org.eclipse.core.resources.IMarker;
	import org.eclipse.core.runtime.CoreException;
	import org.eclipse.emf.common.util.EList;
	import org.eclipse.emf.common.util.URI;
	import org.eclipse.emf.ecore.resource.Resource;
	import org.eclipse.emf.ecore.resource.ResourceSet;
	import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
	import org.eclipse.emf.ecore.util.EcoreUtil;
	import org.eclipse.ui.IMarkerResolution;
	
/**
	 * 
	 * @author Michael Warne
	 *
	 */	

public class DummyNameOnlyQuickFix implements IMarkerResolution {
	
		private URI uri;
		private TracePersistenceAdapter tracePersistenceAdapter;
		private ResourceSet resourceSet;
		private Optional<ArtifactWrapperContainer> awc;
		private String label;
		private ArtifactWrapper art;
		private Resource resourceForArtifacts;
		private ArtifactWrapperContainer container;
//		private String movedToPath;
		private String fullPath;
		private String fileName;

		DummyNameOnlyQuickFix(String label) {
			this.label = label;
		}
		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public void run(IMarker marker) {
			try {
				fullPath = (String) marker.getAttribute("DeltaFullPath");
//				movedToPath = (String)marker.getAttribute("DeltaMovedToPath");
				fileName = (String)marker.getAttribute("FileName");
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			resourceSet = new ResourceSetImpl();
			tracePersistenceAdapter = ExtensionPointHelper.getTracePersistenceAdapter().get();
			awc = tracePersistenceAdapter.getArtifactWrappers(resourceSet);
			art = ArtifactsFactory.eINSTANCE.createArtifactWrapper();
			uri = EcoreUtil.getURI(awc.get());
			resourceForArtifacts = resourceSet.createResource(uri);
			EList<ArtifactWrapper> list = awc.get().getArtifacts();
			container = awc.get();
			int counter = -1;
			for (ArtifactWrapper aw : list) {
				counter ++;
				if(aw.getUri().toString().equals(fullPath)){
					art.setArtifactHandler(aw.getArtifactHandler());
					art.setName(fileName);
					art.setUri(aw.getUri());
					break;
				}												
			}					

			if(art.getUri() != null ){
				container.getArtifacts().set(counter, art);
				resourceForArtifacts.getContents().add(container);
				try {
					resourceForArtifacts.save(null);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			try {
				marker.delete();
			} catch (CoreException e) {
				
				e.printStackTrace();
			}
		}
	}

