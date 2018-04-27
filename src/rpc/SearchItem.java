package rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;
import external.TicketMasterAPI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// example1:
		// response.addHeader("Access-Control-Allow-Origin", "*");
		//
		// //Create a PrintWriter from response such that we can add data to response.
		// PrintWriter out = response.getWriter();
		// if (request.getParameter("username") != null) {
		// //Get the username sent from the client
		// String username = request.getParameter("username");
		// //In the output stream, add something to response body.
		// out.print("Hello " + username);
		// }
		// // Send response back to client
		// out.close();

		// example2: return html page
		// response.setContentType("text/html");
		// response.addHeader("Access-Control-Allow-Origin", "*");
		// PrintWriter out = response.getWriter();
		// out.println("<html><body>");
		// out.println("<h1>This is a HTML page</h1>");
		// out.println("</body></html>");
		// out.close();

		// example3: return json object
		// response.setContentType("application/json");
		// response.addHeader("Access-Control-Allow-Origin", "*");
		String userId = request.getParameter("user_id");
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));

		// String username = "";
		// if (request.getParameter("username") != null) {
		// username = request.getParameter("username");
		// }
		// JSONObject obj = new JSONObject();
		// try {
		// obj.put("username", username);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// PrintWriter out = response.getWriter();
		// out.print(obj);
		// out.close();

		// Term can be empty or null.
		String keyword = request.getParameter("term");

		DBConnection conn = DBConnectionFactory.getConnection();
		List<Item> items = conn.searchItems(lat, lon, keyword);

		Set<String> favorite = conn.getFavoriteItemIds(userId);
		conn.close();
		JSONArray array = new JSONArray();

		try {
			for (Item item : items) {
				JSONObject obj = item.toJSONObject();
				obj.put("favorite", favorite.contains(item.getItemId()));
				array.put(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		RpcHelper.writeJsonArray(response, array);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
