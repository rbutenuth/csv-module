package de.codecentric.mule.csv.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mule.runtime.api.component.location.Location.builder;

import javax.inject.Inject;

import org.junit.Test;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.NumberType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.runtime.api.component.location.Location;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.metadata.MetadataService;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

public class ObjectMetadataResolverTest extends MuleArtifactFunctionalTestCase {

	@Inject
	protected MetadataService metadataService;

	@Override
	protected String getConfigFile() {
		return "test-mule-config.xml";
	}

	@Test
	public void getOutputTypeForSimpleCsv() {
		MetadataResult<ComponentMetadataDescriptor<OperationModel>> metadata = getMetadata("csv-to-java");
		assertTrue(metadata.isSuccess());
		OperationModel model = metadata.get().getModel();
		assertEquals("parse", model.getName());
	    ArrayType output = (ArrayType) model.getOutput().getType();
	    ObjectType record = (ObjectType) output.getType();
	    assertEquals(3, record.getFields().size());
	    assertTrue(record.getFieldByName("name").get().getValue() instanceof StringType);
	    assertTrue(record.getFieldByName("age").get().getValue() instanceof NumberType);
	    assertTrue(record.getFieldByName("weight").get().getValue() instanceof NumberType);
	}

	protected MetadataResult<ComponentMetadataDescriptor<OperationModel>> getMetadata(String flow) {
		Location location = builder().globalName(flow).addProcessorsPart().addIndexPart(0).build();

		return metadataService.getOperationMetadata(location);
	}
}
