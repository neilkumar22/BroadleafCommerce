/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.broadleafcommerce.gwt.client.datasource.dynamic;

import java.util.ArrayList;
import java.util.List;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;

/**
 * 
 * @author jfischer
 *
 */
public class FieldDataSourceWrapper extends DataSource {
	
	protected DataSource delegate;

	public FieldDataSourceWrapper(DataSource delegate) {
		this.delegate = delegate;
		setClientOnly(true);
	}

	@Override
	protected Object transformRequest(DSRequest dsRequest) {
		Criteria criteria = dsRequest.getCriteria();
		String entered = (String) criteria.getValues().get("title");
        
		dsRequest.setUseSimpleHttp(true);
        String requestId = dsRequest.getRequestId ();
        DSResponse response = new DSResponse();
        response.setAttribute ("clientContext", dsRequest.getAttributeAsObject ("clientContext"));
        response.setStatus(0);
        List<Record> records = new ArrayList<Record>();
        for (DataSourceField field : delegate.getFields()) {
        	String title = field.getTitle();
        	if (title == null) {
        		title = field.getName();
        	}
        	if (entered == null || entered.equals("") || (title != null && title.toLowerCase().startsWith(entered.toLowerCase()))) {
	        	Record record = new Record();
	        	for (String attribute : field.getAttributes()) {
	        		record.setAttribute(attribute, field.getAttribute(attribute));
	        	}
	        	records.add(record);
        	}
        }
        Record[] recordArray = new Record[]{};
        recordArray = records.toArray(recordArray);
        response.setData(recordArray);
        response.setTotalRows(recordArray.length);
        
        processResponse(requestId, response);
        
        return dsRequest.getData();
	}
	
}
