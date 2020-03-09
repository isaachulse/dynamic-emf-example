package com.isaachulse.dynamicemfexample;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;

public class DynamicEmfExample {

	public static void main(String[] args) throws IOException {
		// Create EFactory (theCoreFactory)
		EcoreFactory eCoreFactory = EcoreFactory.eINSTANCE;

		// Create EClasses (bookStoreEClass)
		EClass universityEClass = eCoreFactory.createEClass();
		universityEClass.setName("University");

		EClass studentEClass = eCoreFactory.createEClass();
		studentEClass.setName("Student");

		EClass moduleEClass = eCoreFactory.createEClass();
		moduleEClass.setName("Module");

		// Instantiate EPackage with unique URI
		EPackage ePackage = eCoreFactory.createEPackage();
		ePackage.setName("UniversityPackage");
		ePackage.setNsPrefix("university");
		ePackage.setNsURI("http://io.dimitris.simpleresource.universitydsl.ecore");

		// Instantiate ECore package
		EcorePackage eCorePackage = EcorePackage.eINSTANCE;

		// Create EAttributes for classes as specified in the model
		EAttribute firstName = eCoreFactory.createEAttribute();
		firstName.setName("firstName");
		firstName.setEType(eCorePackage.getEString());

		EAttribute lastName = eCoreFactory.createEAttribute();
		lastName.setName("lastName");
		lastName.setEType(eCorePackage.getEString());

		EAttribute name = eCoreFactory.createEAttribute();
		name.setName("name");
		name.setEType(eCorePackage.getEString());

		EAttribute credits = eCoreFactory.createEAttribute();
		credits.setName("credits");
		credits.setEType(eCorePackage.getEInt());

		// Create EReferences
		EReference studentEReference = eCoreFactory.createEReference();
		studentEReference.setName("students");
		studentEReference.setEType(studentEClass);
		studentEReference.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
		studentEReference.setContainment(true);

		EReference moduleEReference = eCoreFactory.createEReference();
		moduleEReference.setName("modules");
		moduleEReference.setEType(moduleEClass);
		moduleEReference.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
		moduleEReference.setContainment(true);

		// Add EAttributes and EReferences to classes
		studentEClass.getEStructuralFeatures().add(firstName);
		studentEClass.getEStructuralFeatures().add(lastName);

		moduleEClass.getEStructuralFeatures().add(name);
		moduleEClass.getEStructuralFeatures().add(credits);

		universityEClass.getEStructuralFeatures().add(studentEReference);
		universityEClass.getEStructuralFeatures().add(moduleEReference);

		// Place classes in EPackage
		ePackage.getEClassifiers().add(universityEClass);
		ePackage.getEClassifiers().add(studentEClass);
		ePackage.getEClassifiers().add(moduleEClass);

		// Obtain EFactory instance
		EFactory eFactory = ePackage.getEFactoryInstance();

		// Dynamic instance of EClasses
		EObject universityObject = eFactory.create(universityEClass);
		EObject student1 = eFactory.create(studentEClass);
		EObject student2 = eFactory.create(studentEClass);

		EObject module1 = eFactory.create(moduleEClass);
		EObject module2 = eFactory.create(moduleEClass);

		// Setting values of attributes
		student1.eSet(firstName, "Alice");
		student1.eSet(lastName, "Bobson");

		student2.eSet(firstName, "Bob");
		student2.eSet(lastName, "Alison");

		module1.eSet(name, "PRBX");
		module1.eSet(credits, 40);

		module2.eSet(name, "MODE");
		module2.eSet(credits, 10);

		universityObject.eSet(studentEReference, Arrays.asList(student1, student2));
		universityObject.eSet(moduleEReference, Arrays.asList(module1, module2));


		// Saving as XML File
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
			    "*", new  XMLResourceFactoryImpl());

		Resource resource = resourceSet.createResource(URI.createURI("./university.xml"));

		// Write to file
		resource.getContents().add(universityObject);

		try{
		    /*
		    * Save the resource
		    */
		      resource.save(null);
		   }catch (IOException e) {
		      e.printStackTrace();
		   }


		// Saving as ECore

		ResourceSet metaResourceSet = new ResourceSetImpl();
		metaResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
			    "ecore", new  XMLResourceFactoryImpl());

		Resource metaResource = metaResourceSet.createResource(URI.createURI("./university.ecore"));

		metaResource.getContents().add(ePackage);

		try {
		     /*
		     * Save the resource
		     */
		     metaResource.save(null);
		    } catch (IOException e) {
		      e.printStackTrace();
		   }
	}
}
