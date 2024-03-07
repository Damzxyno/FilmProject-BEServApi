package util;

import java.io.StringReader;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import dto.CustomPlainTextSerializable;
import javax.servlet.http.HttpServletRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class ContentSerializationContext {
    private final static String APPLICATION_XML = "application/xml";
    private final static String APPLICATION_JSON = "application/json";
    private final static String PLAIN_TEXT = "text/plain"; 
    private final static String DEFAULT_MIME = "*/*";
    private final Logger logger = Logger.getLogger(ContentSerializationContext.class.getName());
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    
    public ContentSerializationContext(HttpServletRequest request, HttpServletResponse response) {
    	this.request = request;
    	this.response = response;
    }
    
    public int getQueryParamInteger(String parameter) {
		var value = request.getParameter(parameter);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (Exception ex) {
				logger.log(Level.SEVERE, "Error parsing parameter ", ex);
			}
		}
		return 0;
	}
    
    public Optional<Integer> getPathIntegerValue(){
    	var requestURI = request.getPathInfo();
    	if (requestURI != null) {
    		var uriParts = requestURI.split("/");
            if (uriParts.length >= 1) {
                try {
                    int value = Integer.parseInt(uriParts[1]);
                    return Optional.of(value);
                } catch (NumberFormatException e) {
                    
                    logger.severe(e.toString());
                }
            }
    	}
        
        return Optional.empty();
    }
    
    public String getQueryParamString (String parameter) {
		var value = request.getParameter(parameter);
		if (value != null && value.isBlank()) {
			return null;
		}
		return value;
	}
	public <T extends CustomPlainTextSerializable<T>> T SerializeRequestObject(Class<T> clazz) {
		var content = retrieveRequestBody();
		if (content != null) {
			var contentType = request.getHeader("Content-Type");
	        if (contentType == null) {
	        	return readJSONRequest(content, clazz);
	        }
	        switch (contentType) {
	            case APPLICATION_XML:
	                return readXMLRequest(content, clazz);
	            case PLAIN_TEXT:
	                return readPlainTextRequest(content, clazz);
	            case APPLICATION_JSON:
	                return readJSONRequest(content, clazz);
	            default:
	            	 return readJSONRequest(content, clazz); 
	        }
		}
		return null;
	}
	
	private String retrieveRequestBody() {
		try {
			var reader = request.getReader();
	        var requestBody = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            requestBody.append(line);
	        }
	        return requestBody.toString();
	        

		} catch (Exception e) {
			logger.severe(()-> e.toString());
		}
		return null;
	}
	
    public <T extends CustomPlainTextSerializable<?>> void serializeResponseObject(T content) {
        var accept = getResponseType();
        switch (accept) {
            case APPLICATION_XML:
                writeXMLResponse(content);
                break;

            case PLAIN_TEXT:
                writePlainTextResponse(content);
                break;
            case APPLICATION_JSON:
                writeJSONResponse(content);
                break;
            default:
            	 writeJSONResponse(content);
                 break;   
        }
    }
    
    private String getResponseType() {
    	var accept = request.getHeader("Accept");
    	if (accept != null && !accept.equals(DEFAULT_MIME)) {
    		return accept;
    	}
    	var contentType = request.getHeader("Content-Type");
    	if (contentType != null) {
    		return contentType;
    	}
    	return APPLICATION_JSON;
    }

    private <T> void writeXMLResponse(T content) {
        try {
        	response.setContentType(APPLICATION_XML);
        	var currClass = content.getClass();
        	var writer = response.getWriter();
        	var context = JAXBContext.newInstance(currClass);
        	var mar = context.createMarshaller();
        	mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        	mar.marshal(content, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> void writeJSONResponse(T content) {
        try {
        	var gson = new Gson();
        	var contentJSON = gson.toJson(content);
        	response.setContentType(APPLICATION_JSON);
            response.getWriter().write(contentJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> void writePlainTextResponse(CustomPlainTextSerializable<?> content) {
        try {
        	response.setContentType(PLAIN_TEXT);
            response.getWriter().write(content.toPlainText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
 
    private <T> T readXMLRequest(String content, Class<T> clazz) {
        try {
            var reader = new StringReader(content);
            var context = JAXBContext.newInstance(clazz);
            var unmarshaller = context.createUnmarshaller();
            return clazz.cast(unmarshaller.unmarshal(reader));
        } catch (JAXBException e) {
            e.printStackTrace();
            return null; // Handle the exception as needed
        }
    }

    private <T> T readJSONRequest(String jsonString, Class<T> clazz) {
        try {
            var gson = new Gson();
            return gson.fromJson(jsonString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
	private <T extends CustomPlainTextSerializable<T>> T readPlainTextRequest(String content, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            return (T) instance.fromPlainText(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}