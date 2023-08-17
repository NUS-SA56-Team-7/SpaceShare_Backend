package com.spaceshare.backend.services.implementations;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.PropertyDoc;
import com.spaceshare.backend.repos.PropertyDocRepository;
import com.spaceshare.backend.services.PropertyDocService;

@Service
public class PropertyDocServiceImpl implements PropertyDocService {
	
	/*** Repositories ***/
	@Autowired
	PropertyDocRepository repoPropertyDoc;
    
	/*** Methods ***/
	@Transactional
	public List<PropertyDoc> createPropertyDocs(Property property, List<String> propertyDocURLs) {
		
		List<PropertyDoc> propertyDocs = new ArrayList<PropertyDoc>();
		
		for (String docURL: propertyDocURLs) {
			PropertyDoc propertyDoc = new PropertyDoc();
			propertyDoc.setDocUrl(docURL);
			propertyDoc.setProperty(property);
			
			propertyDocs.add(repoPropertyDoc.save(propertyDoc));
		}
		
		return propertyDocs;
	}
	
	@Transactional
	public Boolean deletePropertyDocs(Long propertyId) {
		List<PropertyDoc> propertyDocs = repoPropertyDoc.findByPropertyId(propertyId);
		for (PropertyDoc doc: propertyDocs) {
			repoPropertyDoc.deleteById(doc.getId());
		}
		
		return true;
	}
}
