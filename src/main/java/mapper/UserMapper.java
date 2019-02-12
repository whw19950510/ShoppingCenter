

package mapper;


import bean.User;

public interface UserMapper {

    int updateSingleUser(User user);

    User selectSingleUserByName(String name);
}
