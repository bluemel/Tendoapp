package de.bluemel.tendoapp.shared;

public class UserAdmin {

	private static final String[] users = { "martin", "matze", "robert",
			"stephan" };

	public static boolean verify(final String username, final String pwd) {
		boolean usernameKnown = false;
		for (final String un : users) {
			if (un.equals(username)) {
				usernameKnown = true;
				break;
			}
		}
		return usernameKnown && pwd.equals("tendo");
	}
}
