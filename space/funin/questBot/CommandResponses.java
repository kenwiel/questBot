package space.funin.questBot;

public class CommandResponses {

	public static String errorArgAmount = "Incorrect amount of arguments.";
	public static String errorArgMention = "@Mention expected.";
	public static String errorPermissions = "Missing permissions. Requires a moderator Role.";
	public static String errorNull = "Target is null.";
	public static String errorTimeout = "Operation took too long.";
	public static String errorInvalidRoleMissing = "User does not have specified role.";
	public static String errorInvalidRoleUserHasIt = "User already has specified role.";
	public static String errorInvalidRoleNotAValidTarget = "The specified role is not a valid target for this command.";
	public static String errorNoMutedRole = "Muted role does not exist. Please run !!setupMute.";
	public static String errorMessageLength = "Message too long.";
	public static String errorQuestRoleDoesNotExist = "The Role for the Quest does not exist.";
	public static String errorQuestUserDoesNotExist = "The QM for the Quest does not exist.";
}
