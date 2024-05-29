package com.bntushniki.blog.controllers;

import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.service.UserSecurityService;
import com.bntushniki.blog.service.FollowerService;
import com.bntushniki.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserSecurityService userSecurityService;
    private final UserService userService;
    private final FollowerService followerService;

    @Autowired
    public UserController(UserSecurityService userSecurityService, UserService userService, FollowerService followerService) {
        this.userSecurityService = userSecurityService;
        this.userService = userService;
        this.followerService = followerService;
    }

    @GetMapping("/profile")
    public String getCurrentUserProfile(@AuthenticationPrincipal User user) {
        String userLogin = user.getUsername();
        return "redirect:/user/profile/" + userLogin;
    }

    @GetMapping("/profile/{userLogin}")
    public String getProfile(@PathVariable String userLogin, Model model, @AuthenticationPrincipal User currentUser) {
        Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserLogin(userLogin);
        Optional<UserSecurity> currentUserSecurityOptional = userSecurityService.findByUserLogin(currentUser.getUsername());
        if (userSecurityOptional.isEmpty()) {
            return "userNotFound";
        }

        UserSecurity userSecurity = userSecurityOptional.get();
        UserSecurity currentUserSecurity = currentUserSecurityOptional.get();
        Optional<Users> usersOptional = userService.getUserById(userSecurity.getUserId());
        Optional<Users> currentUsersOptional = userService.getUserById(currentUserSecurity.getUserId());

        if (usersOptional.isEmpty()) {
            return "userNotFound";
        }
        Users users = usersOptional.get();
        Users currentUsers = currentUsersOptional.get();

        model.addAttribute("user", users);
        model.addAttribute("email", userSecurity.getEmail());
        model.addAttribute("userLogin", userLogin);
        model.addAttribute("followerCount", followerService.countFollowers(users.getUserId()));
        model.addAttribute("followingCount", followerService.countFollowing(users.getUserId()));
        model.addAttribute("isFollow", followerService.isFollow(currentUsers.getUserId(), users.getUserId()));
        model.addAttribute("currentUser", currentUsers);
        model.addAttribute("isCurrentUser", currentUser.getUsername().equals(userLogin));
        if (userSecurityService.checkRole(userSecurity.getId())) {
            model.addAttribute("teacher", userSecurity.getRole());
        }
        return "profile";
    }

    @PostMapping("/follow")
    public String followUser(@RequestParam String userLogin, @RequestParam Long followerUserId, @RequestParam Long followingUserId) {
        followerService.followUser(followerUserId, followingUserId);
        return "redirect:/user/profile/" + userLogin;
    }

    @PostMapping("/unfollow")
    public String unfollowUser(@RequestParam String userLogin, @RequestParam Long followerUserId, @RequestParam Long followingUserId) {
        followerService.unfollowUser(followerUserId, followingUserId);
        return "redirect:/user/profile/" + userLogin;
    }

    @GetMapping("/profile/followers/{userLogin}")
    public String getFollowers(@PathVariable String userLogin, Model model) {
        Map<Users, String> followersWithLogins = followerService.getFollowerMapWithLogins(userLogin);
        model.addAttribute("followersWithLogins", followersWithLogins);
        return "followers";
    }

    @GetMapping("/profile/following/{userLogin}")
    public String getFollowing(@PathVariable String userLogin, Model model) {
        Map<Users, String> followingWithLogins = followerService.getFollowingMapWithLogins(userLogin);
        model.addAttribute("followingWithLogins", followingWithLogins);
        return "following";
    }

    @GetMapping("/edit")
    public String editPage() {
        return "changeProfileMenu";
    }

    @GetMapping("/edit/error")
    public String editPageError() {
        return "editError";
    }

    @GetMapping("/edit/success")
    public String editPageSuccess() {
        return "editSuccess";
    }

    @GetMapping("/edit/name")
    public String editFirstNameAndLastNameForm() {
        return "editName";
    }

    @PostMapping("/edit/name")
    public String editFirstNameAndLastNameSubmit(@AuthenticationPrincipal User user,
                                                 @RequestParam String firstName, @RequestParam String lastName) {
        if (userService.updateFirstLastName(user, firstName, lastName)) {
            return "redirect:success";
        }
        return "redirect:error";
    }

    @GetMapping("/edit/email")
    public String editEmailForm() {
        return "editEmail";
    }

    @PostMapping("/edit/email")
    public String editEmail(@AuthenticationPrincipal User user, @RequestParam String email) {
        if(userService.updateEmail(user, email)){
            return "redirect:success";
        }
        return "redirect:error";
    }

    @GetMapping("/edit/resources")
    public String editResourcesForm() {
        return "editResources";
    }

    @PostMapping("/edit/resources")
    public String editResources(@AuthenticationPrincipal User user, @RequestParam String website,
                                      @RequestParam String github) {
        if(userService.updateResource(user, website, github)) {
            return "redirect:success";
        }
        return "redirect:error";
    }

    @GetMapping("/edit/phone")
    public String editPhoneForm() {
        return "editPhone";
    }

    @PostMapping("/edit/phone")
    public String editPhone(@AuthenticationPrincipal User user, @RequestParam String phone) {
        if(userService.updatePhone(user, phone)){
            return "redirect:success";
        }
        return "redirect:error";
    }

    @GetMapping("/edit/password")
    public String editPasswordForm() {
        return "editPassword";
    }

    @PostMapping("/edit/password")
    public String editPassword(@AuthenticationPrincipal User user, @RequestParam String password, @RequestParam String
                               confirmPassword) {
        if(userService.updatePassword(user, password, confirmPassword)){
            return "redirect:success";
        }
        return "redirect:error";
    }
}
