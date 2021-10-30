package sg.edu.smu.cs203.pandanews.controller;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import sg.edu.smu.cs203.pandanews.service.organisation.OrganisationService;
import sg.edu.smu.cs203.pandanews.service.workgroup.WorkGroupService;
import sg.edu.smu.cs203.pandanews.service.user.UserService;
import sg.edu.smu.cs203.pandanews.model.user.User;
import sg.edu.smu.cs203.pandanews.model.Organisation;
import sg.edu.smu.cs203.pandanews.model.WorkGroup;
import org.springframework.beans.factory.annotation.Autowired;

public class UserController {
    private UserService userService;
    private WorkGroup workGroupService;
    private OrganisationService orgService;

    @Autowired
    public UserController(UserService us, OrganisationService orgs, WorkGroup workGroupService){
        this.userService = us;
        this.orgService = orgs;
        this.workGroupService = workGroupService;
    }

    /**
     * List all books in the system
     * @return list of all books
     */
    @GetMapping("/users")
    public List<User> getUsers(){
        return userService.listUsers();
    }

    /**
     * Search for book with the given id
     * If there is no book with the given "id", throw a BookNotFoundException
     * @param id
     * @return book with the given id
     */
    @GetMapping("/users/profile")
    public User getUserProfile(){
        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        if(user == null) return null;
        return user;
    }
    /**
     * Add a new book with POST request to "/books"
     * Note the use of @RequestBody
     * @param user
     * @return list of all books
     */
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("/users")
//    public User addUser(@RequestBody User user){
//        return userService.addUser(user);
//    }

    /**
     * If there is no book with the given "id", throw a BookNotFoundException
     * @param id
     * @param newUserInfo
     * @return the updated, or newly added book
     */
    @PutMapping("/users/profile")
    public User updateUser(@RequestBody User newUserInfo){
        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
        
        User user = userService.getUserByUsername(userDetails.getUsername());
        if(user == null) return null;
        System.out.println(user.getId());
        user = userService.updateUser(user.getId(), newUserInfo);
        if(user == null) return null;
        
        return user;
    }

    /**
     * Remove a book with the DELETE request to "/books/{id}"
     * If there is no book with the given "id", throw a BookNotFoundException
     * @param id
     */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id){
        try{
            userService.deleteUser(id);
         }catch(EmptyResultDataAccessException e) {
            // throw new BookNotFoundException(id);
         }
    }
    
    @GetMapping("/users/organisation")
    public Organisation getUserOrganisation(){
        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
        
        User user = userService.getUserByUsername(userDetails.getUsername());
        if(user == null) return null;

        return user.getOrganisation();
    }

    @PostMapping("/users/organisation")
    public User addUserOrganisation(@RequestBody Organisation organisation){
        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
        
        User user = userService.getUserByUsername(userDetails.getUsername());
        if(user == null) return null;

        Organisation org = orgService.getOrganisationByCode(organisation.getCode());
        if(org == null) return null;

        return userService.joinOrganisation(user, org);
    }

    @GetMapping("/users/workgroup")
    public WorkGroup getWorkGroup(){
        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
        
        User user = userService.getUserByUsername(userDetails.getUsername());
        if(user == null) return null;

        return user.getWorkGroup();
    }

    @PostMapping("/users/workgroup")
    public User addUserWorkGroup(@RequestBody WorkGroup workGroup, @RequestBody User user){
        user = userService.getUserByUsername(user.getUsername());
        if(user == null) return null;

        WorkGroup wg = workGroupService.getWorkGroup(workGroup.getId());
        if(wg == null) return null;

        return userService.joinWorkGroup(user, wg);
    }

    @PutMapping("/users/vaccine")
    public User updateUserVaacine(){
        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
        
        User user = userService.getUserByUsername(userDetails.getUsername());
        if(user == null) return null;

        return userService.updateVaccine(user);
    }
}
