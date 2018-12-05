/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.example.json.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.jackrabbit.vault.util.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.jcr.JsonItemWriter;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.day.cq.dam.api.DamConstants;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service=Servlet.class,
           property={
                   Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet",
                   "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                   "sling.servlet.resourceTypes="+ "cq/Page",
                   "sling.servlet.extensions=" + "json",
                   "sling.servlet.selectors=" + "data"
                   
           })
public class SimpleServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUid = 1L;

    @SuppressWarnings("deprecation")
	@Override
    protected void doGet(final SlingHttpServletRequest request,
            final SlingHttpServletResponse response) throws ServletException, IOException {
    	  response.setCharacterEncoding("UTF-8");
          response.setContentType("application/json");

          final PrintWriter out = response.getWriter();
          final ResourceResolver resolver = request.getResourceResolver();
         System.out.println( request.getResource().getPath());
          final Resource resource = resolver.getResource(request.getResource().getPath()+"/jcr:content");
          final Node node = resource.adaptTo(Node.class);

          // Node properties to exclude from the JSON object. 
          final Set<String> propertiesToIgnore = new HashSet<String>() {{
              add("jcr:created");
              add("jcr:createdBy");
              add("jcr:versionHistory");
              add("jcr:predecessors");
              add("jcr:baseVersion");
              add(DamConstants.PN_PAGE_LAST_REPLICATED);
              add(DamConstants.PN_PAGE_LAST_REPLICATED_BY);
              add(DamConstants.PN_PAGE_LAST_REPLICATION_ACTION);          
              add(DamConstants.PN_VERSION_COMMENT);
              add(DamConstants.PN_VERSION_CREATOR);
              add("cq:lastModified");

              

          }};
          for(Field field:JcrConstants.class.getFields())
          {
        	  try {
        		  if(field.get(null)!=null) {
				propertiesToIgnore.add(field.get(null).toString());
        		  }
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }
          JsonItemWriter jsonWriter = new JsonItemWriter(propertiesToIgnore);

          try {
              // Write the JSON to the PrintWriter with max recursion of 1 level and tidy formatting. 
              jsonWriter.dump(node, out, -1, true);
              response.setStatus(SlingHttpServletResponse.SC_OK);
          } catch (RepositoryException | JSONException e) {
              //logger.error("Could not get JSON", e);
              response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          }
      }
    }

