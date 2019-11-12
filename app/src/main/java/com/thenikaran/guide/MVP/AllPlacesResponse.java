package com.thenikaran.guide.MVP;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllPlacesResponse {

private List<Place> place = null;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public List<Place> getPlace() {
return place;
}

public void setPlace(List<Place> place) {
this.place = place;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}
