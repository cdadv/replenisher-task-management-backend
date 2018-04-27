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
    userAdmin.setCorporation(corporationDemo);
    userAdmin.setAllowedRoleSet(new HashSet<>(Arrays.asList(roleAdmin)));
    userAdmin.setEnabled(true);
    userAdmin.setAccountNonExpired(true);
    userAdmin.setAccountNonLocked(true);
    userAdmin.setCredentialsNonExpired(true);

    /** User staff user initialization. */
    User userStaff = new User();
    userStaff.setUsername("staff_user");
    // TODO: encrypt the password
    userStaff.setPassword(new BCryptPasswordEncoder().encode("staff_password"));
    userStaff.setCorporation(corporationDemo);
    userStaff.setAllowedRoleSet(new HashSet<>(Arrays.asList(roleUserStaff)));
    userStaff.setEnabled(true);
    userAdmin.setAccountNonExpired(true);
    userAdmin.setAccountNonLocked(true);
    userAdmin.setCredentialsNonExpired(true);

    /** User manager user initialization. */
    User userManager = new User();
    userManager.setUsername("manager_user");
    // TODO: encrypt the password
    userManager.setPassword(new BCryptPasswordEncoder().encode("manager_password"));
    userManager.setCorporation(corporationDemo);
    userManager.setAllowedRoleSet(new HashSet<>(Arrays.asList(roleUserManager)));
    userManager.setEnabled(true);
    userAdmin.setAccountNonExpired(true);
    userAdmin.setAccountNonLocked(true);
    userAdmin.setCredentialsNonExpired(true);

    userRepository.saveAll(Arrays.asList(userAdmin, userStaff, userManager));

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
