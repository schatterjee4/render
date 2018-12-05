package com.example.json.core.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

public class UserMultiComponent extends WCMUsePojo {
	 
private static final Logger LOGGER = LoggerFactory.getLogger(UserMultiComponent.class);
private List<UserBean> submenuItems = new ArrayList<UserBean>();
 
@Override
public void activate() throws Exception {
setMultiFieldItems();
}
 
/**
* Method to get Multi field data
* @return submenuItems
*/
private List<UserBean> setMultiFieldItems() {
 
@SuppressWarnings("deprecation")
JSONObject jObj;
try{
String[] itemsProps = getProperties().get("myUserSubmenu", String[].class);

if (itemsProps == null) {
	
	LOGGER.info("PROPS IS NULL") ; 
}


if (itemsProps != null) {
for (int i = 0; i < itemsProps.length; i++) {
 
jObj = new JSONObject(itemsProps[i]);
UserBean menuItem = new UserBean();
 
String title = jObj.getString("title");
String path = jObj.getString("path");
String activity = jObj.getString("activity");
String text = jObj.getString("text");
 
menuItem.setTitle(title);
menuItem.setPath(path);
menuItem.setFlag(activity);
menuItem.setText(text);
submenuItems.add(menuItem);

LOGGER.info("TEXT IS "+text) ;
}
}
}catch(Exception e){
LOGGER.error("Exception while Multifield data {}", e.getMessage(), e);
}
return submenuItems;
}
 
public List<UserBean> getMultiFieldItems() {
return submenuItems;
}
}
