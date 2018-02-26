package eb.test;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
 
@Path("/hello")
public class Hello {
    public static AmazonDynamoDB dynamoDB;
	@GET
	@Path("")
	@Produces("application/json")
	public Response getHello() {
		return Response.status(200).entity("Hello").build();
	}
	
	@GET
	@Path("/{param}")
	@Produces("application/json")
	public Response getMsg(@PathParam("param") String msg) {
		String output = "getMsg : " + msg;
		return Response.status(200).entity(output).build();
 
	}
	
	@GET
	@Path("/table")
	@Produces("application/json")
	public Response getTable(@PathParam("param") String msg) {

        String tableName = "my-favorite-movies-table";
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        TableDescription tableDescription = getDynamoDBClient().describeTable(describeTableRequest).getTable();
		return Response.status(200).entity(tableDescription.getTableName()).build();
 
	}
	
	@GET
	@Path("/table/{param}")
	@Produces("application/json")
	public Response getTableParam(@PathParam("param") String msg) {
        String tableName = "my-favorite-movies-table";
        Map<String, AttributeValue> item = newItem(msg, 1989, "****", "James", "Sara");
        PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
        PutItemResult putItemResult = getDynamoDBClient().putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
		return Response.status(200).entity("Result:" + putItemResult).build();
 
	}
	
    @Path("/post")
    @POST
	@Produces("application/json")
    public Response table(final String postBody) {
		String output = "table : " + postBody;
		return Response.status(200).entity(output).build();
    }
    
    private Map<String, AttributeValue> newItem(String name, int year, String rating, String... fans) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("name", new AttributeValue(name));
        item.put("year", new AttributeValue().withN(Integer.toString(year)));
        item.put("rating", new AttributeValue(rating));
        item.put("fans", new AttributeValue().withSS(fans));

        return item;
    }
    
    private AmazonDynamoDB getDynamoDBClient() {
        if(dynamoDB == null) {
            dynamoDB = AmazonDynamoDBClientBuilder.standard()
                .build();
        }
        return dynamoDB;
    }
}
