package space.funin.questBot.runnables;

import java.util.concurrent.ExecutionException;

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
		System.out.println("Unmuting " + user.getName());
		try {
			muted.removeUser(user).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
}