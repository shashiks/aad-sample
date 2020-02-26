package sample.aad.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.filter.GenericFilterBean;

import com.microsoft.graph.auth.confidentialClient.ClientCredentialProvider;
import com.microsoft.graph.auth.enums.NationalCloud;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.extensions.Group;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IGroupCollectionPage;
import com.microsoft.graph.requests.extensions.IGroupCollectionRequest;
import com.microsoft.graph.requests.extensions.IGroupCollectionRequestBuilder;

public class MyFilter extends GenericFilterBean {

	public static String CLIENT_ID = "86cdbd02-dba5-4920-ba3a-120b97fc948b";
	public static String SCOPE = "https://graph.microsoft.com/.default";
	public static List<String> SCOPES = Arrays.asList(SCOPE);
	public static String CLIENT_SECRET = "4uajJ3RuN@ZSiA/[mO.mdSo4rwj26:n=";
	public static String TENANT = "cirrusplcs.onmicrosoft.com";

	public static String CLIENT_ASSERTION = "CLIENT_ASSERTION";
	public static NationalCloud NATIONAL_CLOUD = NationalCloud.Global;
//	public static String tenantGUID = "TENANT_GUID";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		ClientCredentialProvider authProvider = new ClientCredentialProvider(CLIENT_ID, SCOPES, CLIENT_SECRET, TENANT,
				NATIONAL_CLOUD);

		IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(authProvider)
				.buildClient();
		

//		User user = graphClient.me().buildRequest().get();

		// Set up the initial request builder and build request for the first page
		IGroupCollectionRequestBuilder groupsRequestBuilder = graphClient.groups();
		IGroupCollectionRequest groupsRequest = groupsRequestBuilder.buildRequest();

		try {
			// Execute the request
			IGroupCollectionPage groupsCollection = groupsRequest.get();

			// Process each of the items in the response
			for (Group group : groupsCollection.getCurrentPage()) {
				System.out.println(group.displayName);
			}

			// Build the request for the next page, if there is one
			groupsRequestBuilder = groupsCollection.getNextPage();
			if (groupsRequestBuilder == null) {
				groupsRequest = null;
			} else {
				groupsRequest = groupsRequestBuilder.buildRequest();
			}

		} catch (Exception ex) {
			
			// Handle failure
			ex.printStackTrace();
			groupsRequest = null;
		}

//		HttpServletRequest httpReq = (HttpServletRequest) request;
//		chain.doFilter(request, response);
//		System.out.println("request.getHeader(TOKEN_HEADER) " + httpReq.getHeader("Authorization"));
//		try {
//			System.out.println("httpReq.getSession() " + httpReq.getSession().getId());
//		} catch (Exception e) {
//			System.out.println("Session out" + e);
//		}
//		System.out.println("httpReq.getCookies() " + httpReq.getCookies());
//		if (httpReq.getCookies() != null) {
//			System.out.println("	httpReq.getCookies().length; " + httpReq.getCookies().length);
//		}
//
//		Object c = SecurityContextHolder.getContext();
//		if (c != null) {
//
//			Authentication a = SecurityContextHolder.getContext().getAuthentication();
//			if (c != null) {
//				System.out.println("Authentication  from Context  " + a);
//				System.out.println("a.getAuthorities().toString()  " + a.getAuthorities().toString());
//				Object principal = a.getPrincipal();
//				System.out.println("princi  " + principal);
//				if (principal instanceof UserDetails) {
//					String username = ((UserDetails) principal).getUsername();
//				} else {
//					String username = principal.toString();
//				}
//
//			}
//		}
//		logger.info("< doFilter");
//
//		// chain.doFilter(request, response);

	}
}