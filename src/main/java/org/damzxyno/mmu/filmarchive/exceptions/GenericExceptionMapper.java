//package org.damzxyno.mmu.filmarchive.exceptions;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;
//import javax.ws.rs.ext.ExceptionMapper;
//import javax.ws.rs.ext.Provider;
//import org.damzxyno.mmu.filmarchive.dto.ErrorMessage;
//
//@Provider
//public class GenericExceptionMapper implements ExceptionMapper<Throwable>{
//	private static final Logger logger = Logger.getLogger(GenericExceptionMapper.class.getName());
//	private static final String DEFAULT_MESSAGE = "An error occured, please try again latter";
//	@Override
//	public Response toResponse(Throwable exception) {
//		logger.log(Level.SEVERE, "INTERNAL SERVER ERROR", exception);
//		var message = new ErrorMessage(DEFAULT_MESSAGE, Status.INTERNAL_SERVER_ERROR.getStatusCode(), "NIL");
//		return Response
//				.status(Status.INTERNAL_SERVER_ERROR)
//				.entity(message)
//				.build();
//	}
//}
