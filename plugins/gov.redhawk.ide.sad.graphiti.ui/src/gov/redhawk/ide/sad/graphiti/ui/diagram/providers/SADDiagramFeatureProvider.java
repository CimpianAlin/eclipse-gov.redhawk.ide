package gov.redhawk.ide.sad.graphiti.ui.diagram.providers;

import gov.redhawk.ide.sad.graphiti.ui.diagram.patterns.ComponentPattern;
import gov.redhawk.ide.sad.graphiti.ui.diagram.patterns.FindByNamingServicePattern;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.pattern.DefaultFeatureProviderWithPatterns;



public class SADDiagramFeatureProvider extends DefaultFeatureProviderWithPatterns {

	
	public SADDiagramFeatureProvider(IDiagramTypeProvider diagramTypeProvider){
		super(diagramTypeProvider);
		
		//Add Patterns for Domain Objects
		addPattern(new ComponentPattern());
		addPattern(new FindByNamingServicePattern());

	}
	
	@Override
	public IAddFeature getAddFeature(IAddContext context){
		
//		if(context.getNewObject() instanceof myObject1){
//			return new Feature1(this);
//		}
//		else if(context.getNewObject() instanceof myObject2){
//			return new Feature2(this);
//		}
		
		return super.getAddFeature(context);
	}
	
	@Override
	public ICreateFeature[] getCreateFeatures(){
		
		ICreateFeature[] defaultCreateFeatures = super.getCreateFeatures();
		
		//features added via pattern
		return defaultCreateFeatures;
	}
	
	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context){
//		PictogramElement pictogramElement = context.getPictorgramElement();
//		if(pictogramElement instanceof ContainerShape){
//			Object bo = getBusinessObjectForPictogramElement(pictogramElement);
//			if(bo instanceof Object1){
//				return new UpdateObjec1Feature(this);
//			}
//		}
		
		return super.getUpdateFeature(context);
	}
	
	@Override
	public IDeleteFeature getDeleteFeature(IDeleteContext context){
		
		return super.getDeleteFeature(context);
	}
	
	
	@Override
	public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context){
		
		return super.getResizeShapeFeature(context);
	}
}
