package space.funin.questBot.runnables;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;

public class RunnableUnmute implements Runnable{

	private User user;
	private Role muted;
	
	public RunnableUnmute(User user, Role muted) {
		this.user = user;
		this.muted = muted;
	}
	
	@Override
	public void run() {
		muted.removeUser(user);
	}

	
	
}