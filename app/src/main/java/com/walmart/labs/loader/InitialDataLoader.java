package com.walmart.labs.loader;

import com.walmart.labs.domain.Corporation;
import com.walmart.labs.domain.RolePrivilege;
import com.walmart.labs.domain.User;
import com.walmart.labs.domain.UserRole;
import com.walmart.labs.repository.CorporationRepository;
import com.walmart.labs.repository.RolePrivilegeRepository;
import com.walmart.labs.repository.UserRepository;
import com.walmart.labs.repository.UserRoleRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
  private boolean alreadySetup = false;

  @Autowired private CorporationRepository corporationRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private UserRoleRepository userRoleRepository;
  @Autowired private RolePrivilegeRepository rolePrivilegeRepository;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (alreadySetup) {
      return;
    }
    /** Admin role should have all the access to GET, POST, PUT, DELETE for all */
    RolePrivilege privilegeAdminAllGet = createPrivilegeIfNotFound("PRIVILEGE_ADMIN_ALL_GET");
    RolePrivilege privilegeAdminAllPost = createPrivilegeIfNotFound("PRIVILEGE_ADMIN_ALL_POST");
    RolePrivilege privilegeAdminAllPut = createPrivilegeIfNotFound("PRIVILEGE_ADMIN_ALL_PUT");
    RolePrivilege privilegeAdminAllDelete = createPrivilegeIfNotFound("PRIVILEGE_ADMIN_ALL_DELETE");

    /**
     * Staff GET task privilege contains query all the tasks that are assigned to the staff. A staff
     * will only be able to check his/her own tasks. He/She is not authorized to query others'
     * tasks.
     */
    RolePrivilege privilegeUserStaffTaskGet =
        createPrivilegeIfNotFound("PRIVILEGE_USER_STAFF_TASK_GET");
    /**
     * Staff POST task privilege contains add tasks for the staff himself/herself. He/She is not
     * authorized to add tasks for others.
     */
    RolePrivilege privilegeUserStaffTaskPost =
        createPrivilegeIfNotFound("PRIVILEGE_USER_STAFF_TASK_POST");
    /**
     * Staff PUT task privilege contains edit tasks for the staff himself/herself. He/She is not
     * authorized to edit tasks for others.
     */
    RolePrivilege privilegeUserStaffTaskPut =
        createPrivilegeIfNotFound("PRIVILEGE_USER_STAFF_TASK_PUT");
    /** Staff does not have DELETE privileges. They are not authorized to delete their own tasks. */
    //    RolePrivilege privilegeUserStaffTaskDelete =
    //        createPrivilegeIfNotFound("PRIVILEGE_USER_STAFF_TASK_DELETE");

    /**
     * Manager GET task privilege contains query all the tasks that they managed of the corporation.
     * (Set as reporter)
     */
    RolePrivilege privilegeUserManagerTaskGet =
        createPrivilegeIfNotFound("PRIVILEGE_USER_MANAGER_TASK_GET");
    /**
     * Manager POST task privilege contains add(assign) tasks for all the staffs of the corporation.
     */
    RolePrivilege privilegeUserManagerTaskPost =
        createPrivilegeIfNotFound("PRIVILEGE_USER_MANAGER_TASK_POST");
    /** Manager PUT task privilege contains edit tasks for all the staffs of the corporation. */
    RolePrivilege privilegeUserManagerTaskPut =
        createPrivilegeIfNotFound("PRIVILEGE_USER_MANAGER_TASK_PUT");
    /**
     * Manager DELETE task privilege contains delete tasks for all the staffs of the corporation.
     */
    RolePrivilege privilegeUserManagerTaskDelete =
        createPrivilegeIfNotFound("PRIVILEGE_USER_MANAGER_TASK_DELETE");

    /**
     * Admin role contains privilegeAdminAllGet, privilegeAdminAllPost, privilegeAdminAllPut,
     * privilegeAdminAllDelete
     */
    Set<RolePrivilege> privilegeAdminSet =
        new HashSet<>(
            Arrays.asList(
                privilegeAdminAllGet,
                privilegeAdminAllPost,
                privilegeAdminAllPut,
                privilegeAdminAllDelete));
    UserRole roleAdmin = createRoleIfNotFound("ROLE_ADMIN", privilegeAdminSet);

    /**
     * User staff role contains privilegeUserStaffTaskGet, privilegeUserStaffTaskPost,
     * privilegeUserStaffTaskPut
     */
    Set<RolePrivilege> privilegeUserStaffSet =
        new HashSet<>(
            Arrays.asList(
                privilegeUserStaffTaskGet, privilegeUserStaffTaskPost, privilegeUserStaffTaskPut));
    UserRole roleUserStaff = createRoleIfNotFound("ROLE_USER_STAFF", privilegeUserStaffSet);

    /**
     * manager role contains privilegeUserManagerTaskGet, privilegeUserManagerTaskPost,
     * privilegeUserManagerTaskPut, privilegeUserManagerTaskDelete
     */
    Set<RolePrivilege> privilegeUserManagerSet =
        new HashSet<>(
            Arrays.asList(
                privilegeUserManagerTaskGet,
                privilegeUserManagerTaskPost,
                privilegeUserManagerTaskPut,
                privilegeUserManagerTaskDelete));
    UserRole roleUserManager = createRoleIfNotFound("ROLE_USER_MANAGER", privilegeUserManagerSet);

    Corporation corporationDemo = createCorporationIfNotFound("CORPORATION_DEMO");

    /** Admin user initialization. */
    User userAdmin = new User();
    userAdmin.setUsername("admin_user");
    // TODO: encrypt the password
    userAdmin.setPassword(new BCryptPasswordEncoder().encode("admin_password"));
    userAdmin.setFullName("admin");
    userAdmin.setEmailAddress("admin@admin.com");
    userAdmin.setCorporation(corporationDemo);
    userAdmin.setAllowedRoleSet(new HashSet<>(Arrays.asList(roleAdmin)));
    userAdmin.setEnabled(true);
    userAdmin.setAccountNonExpired(true);
    userAdmin.setAccountNonLocked(true);
    userAdmin.setCredentialsNonExpired(true);

    /** User staff user 1 initialization. */
    User userStaff1 = new User();
    userStaff1.setUsername("staff_user_1");
    // TODO: encrypt the password
    userStaff1.setPassword(new BCryptPasswordEncoder().encode("staff_password_1"));
    userStaff1.setFullName("staff1");
    userStaff1.setEmailAddress("staff1@staff1.com");
    userStaff1.setCorporation(corporationDemo);
    userStaff1.setAllowedRoleSet(new HashSet<>(Arrays.asList(roleUserStaff)));
    userStaff1.setEnabled(true);
    userStaff1.setAccountNonExpired(true);
    userStaff1.setAccountNonLocked(true);
    userStaff1.setCredentialsNonExpired(true);

    /** User staff user 2 initialization. */
    User userStaff2 = new User();
    userStaff2.setUsername("staff_user_2");
    // TODO: encrypt the password
    userStaff2.setPassword(new BCryptPasswordEncoder().encode("staff_password_2"));
    userStaff2.setFullName("staff2");
    userStaff2.setEmailAddress("staff2@staff2.com");
    userStaff2.setCorporation(corporationDemo);
    userStaff2.setAllowedRoleSet(new HashSet<>(Arrays.asList(roleUserStaff)));
    userStaff2.setEnabled(true);
    userStaff2.setAccountNonExpired(true);
    userStaff2.setAccountNonLocked(true);
    userStaff2.setCredentialsNonExpired(true);

    /** User staff user 3 initialization. */
    User userStaff3 = new User();
    userStaff3.setUsername("staff_user_3");
    // TODO: encrypt the password
    userStaff3.setPassword(new BCryptPasswordEncoder().encode("staff_password_3"));
    userStaff3.setFullName("staff3");
    userStaff3.setEmailAddress("staff3@staff3.com");
    userStaff3.setCorporation(corporationDemo);
    userStaff3.setAllowedRoleSet(new HashSet<>(Arrays.asList(roleUserStaff)));
    userStaff3.setEnabled(true);
    userStaff3.setAccountNonExpired(true);
    userStaff3.setAccountNonLocked(true);
    userStaff3.setCredentialsNonExpired(true);

    /** User manager user 1 initialization. */
    User userManager1 = new User();
    userManager1.setUsername("manager_user_1");
    // TODO: encrypt the password
    userManager1.setPassword(new BCryptPasswordEncoder().encode("manager_password_1"));
    userManager1.setFullName("manager1");
    userManager1.setEmailAddress("manager1@manager1.com");
    userManager1.setCorporation(corporationDemo);
    userManager1.setAllowedRoleSet(new HashSet<>(Arrays.asList(roleUserManager)));
    userManager1.setEnabled(true);
    userManager1.setAccountNonExpired(true);
    userManager1.setAccountNonLocked(true);
    userManager1.setCredentialsNonExpired(true);

    /** User manager user 2 initialization. */
    User userManager2 = new User();
    userManager2.setUsername("manager_user_2");
    // TODO: encrypt the password
    userManager2.setPassword(new BCryptPasswordEncoder().encode("manager_password_2"));
    userManager1.setFullName("manager2");
    userManager1.setEmailAddress("manager2@manager2.com");
    userManager2.setCorporation(corporationDemo);
    userManager2.setAllowedRoleSet(new HashSet<>(Arrays.asList(roleUserManager)));
    userManager2.setEnabled(true);
    userManager2.setAccountNonExpired(true);
    userManager2.setAccountNonLocked(true);
    userManager2.setCredentialsNonExpired(true);

    userRepository.saveAll(
        Arrays.asList(userAdmin, userStaff1, userStaff2, userStaff3, userManager1, userManager2));

    alreadySetup = true;
  }

  @Transactional
  RolePrivilege createPrivilegeIfNotFound(String name) {

    RolePrivilege privilege = rolePrivilegeRepository.findByName(name);
    if (privilege == null) {
      privilege = new RolePrivilege(name);
      rolePrivilegeRepository.save(privilege);
    }
    return privilege;
  }

  @Transactional
  UserRole createRoleIfNotFound(String name, Set<RolePrivilege> privilegeSet) {

    UserRole role = userRoleRepository.findByName(name);
    if (role == null) {
      role = new UserRole(name);
      role.setAllowedPrivilegeSet(privilegeSet);
      userRoleRepository.save(role);
    }
    return role;
  }

  @Transactional
  Corporation createCorporationIfNotFound(String name) {
    Corporation corporation = corporationRepository.findByName(name);
    if (corporation == null) {
      corporation = new Corporation(name);
      corporationRepository.save(corporation);
    }
    return corporation;
  }
}
