package databasemanager;

import model.User;
import java.util.HashMap;

public interface UserDAO {
	void getLatestUserId();

	boolean checkUserNameExists(String userName);

	boolean saveUserInDatabase(User user);

	boolean updateUserInDatabase(User user);

	HashMap<String, Object> loginUser(String userName, String password);

	boolean addIsVip(int userId);
}
