package bytron.mipueblo.repository;

import bytron.mipueblo.domain.Role;

import java.util.Collection;

//public interface RoleRepository <T extends Role> {
public interface RoleRepository <T extends Role> {
    /* Basic CRUD operations */
    T create(T data);

    //read a lot
//    Collection<T> list(int page, int pageSize);

    //load a specific amount of roles
//    Collection<T> list(int limit);

    //load all the roles because there's not a lot
    Collection<T> list();

    //read just 1
    T get(Long id);

    T update(T data);
    Boolean delete(Long id);

    /* More complex operations */

    void addRoleToUser(Long userId, String roleName);
    Role getRoleByUserId(Long userId);
    Role getRoleByUserEmail(String email);
    void updateUserRole(Long userId, String roleName);



}
