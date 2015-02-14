package jersey.client;

import java.net.URI;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import jersey.server.java.ToDo;

public class JerseyClient {
	public static void main(String[] args) {		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(getBaseURI());
		
		/**
		 * BASIC GET REQUEST
		 */
		System.out.println("\nBasic Get Request:");
		Invocation.Builder invocationBuilder = target.path("example").path("todos").request(MediaType.APPLICATION_XML);
		Response response = invocationBuilder.get();
		System.out.println("GET completed with a status of: " + response.getStatus());				// Get the status of the response
		System.out.println("Contents of the GET request are below:");
		System.out.println(response.readEntity(String.class));										// Get the contents of the response
		
		/**
		 * EXAMPLE OF USER INPUT AFFECTING REQUEST
		 */
		System.out.println("\nBasic Get Request with User Input:");
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);	
		System.out.println("Please enter the ID number of the ToDo item you would like to see:");	// Prompt user for input
		int selection = in.nextInt();																// Get user input
		invocationBuilder = target.path("example").path("todos/"+selection).request(MediaType.APPLICATION_XML);
		Response inputResponse = invocationBuilder.get();											// Grab the response
		System.out.println("GET completed with a status of: " + inputResponse.getStatus());			// Get the status of the response
		System.out.println("Contents of the GET request are below:");
		System.out.println(inputResponse.readEntity(String.class));									// Get the contents of the response
		
		/**
		 * EXAMPLE OF A PUT REQUEST
		 */
		System.out.println("\nPut Request for To-Do Item with ID=5:");
		ToDo todo = new ToDo("5", "Just testing the PUT requests!");		
		Response testResponse = target.path("example").path("todos").path(todo.getId()).request().put(Entity.entity(todo, MediaType.APPLICATION_XML));	
		System.out.println("PUT completed with a status of: " + testResponse.getStatus());			// Get the status of the response
		
		/**
		 * EXAMPLE GET REQUEST OF RECENTLY 'PUT' TO-DO
		 */
		System.out.println("\nGet Request for To-Do Item with ID=5:");
		invocationBuilder = target.path("example").path("todos/5").request(MediaType.APPLICATION_XML);
		Response lastResponse = invocationBuilder.get();											// Grab the response
		System.out.println("GET completed with a status of: " + lastResponse.getStatus());			// Get the status of the response
		System.out.println("Contents of the GET request are below:");
		System.out.println(lastResponse.readEntity(String.class));									// Get the contents of the response
		
		/**
		 * EXAMPLE OF A DELETE REQUEST
		 */
		System.out.println("\nDelete Request for To-Do Item with ID=5:");
		Response deleteResponse = target.path("example").path("todos/5").request().accept(MediaType.APPLICATION_XML).delete();
		System.out.println("Deletion completed with a status of: " + deleteResponse.getStatus());												// Get the status of the response
		
		System.out.println("\nAttempting to find deleted todo item: ");
		invocationBuilder = target.path("example").path("todos/5").request(MediaType.APPLICATION_XML);
		Response isItThere = invocationBuilder.get();											// Grab the response
		System.out.println("GET completed with a status of: " + isItThere.getStatus());
		System.out.println("Contents of the GET request are below:");
		System.out.println(isItThere.readEntity(String.class));									// Read the response
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/jersey.server").build();
	}
}