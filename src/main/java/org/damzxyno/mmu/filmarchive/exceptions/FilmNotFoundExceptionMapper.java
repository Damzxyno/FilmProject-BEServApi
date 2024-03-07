//package org.damzxyno.mmu.filmarchive.exceptions;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;
//import javax.ws.rs.ext.ExceptionMapper;
//import javax.ws.rs.ext.Provider;
//import org.damzxyno.mmu.filmarchive.dto.ErrorMessage;
//
//@Provider
//public class FilmNotFoundExceptionMapper implements ExceptionMapper<FilmNotFoundException>{
//
//	@Override
//	public Response toResponse(FilmNotFoundException exception) {
//		var message = new ErrorMessage(exception.getMessage(), Status.NOT_FOUND.getStatusCode(), "NIL");
//		return Response
//				.status(Status.NOT_FOUND)
//				.entity(message)
//				.build();
//	}
//}
