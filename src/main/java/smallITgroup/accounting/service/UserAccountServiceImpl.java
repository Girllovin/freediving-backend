package smallITgroup.accounting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import smallITgroup.accounting.dao.UserAccountRepository;
import smallITgroup.accounting.dto.UserDto;
import smallITgroup.accounting.dto.UserInfoDto;
import smallITgroup.accounting.dto.UserRegisterDto;
import smallITgroup.accounting.dto.exeptions.*;
import smallITgroup.accounting.model.UserAccount;

enum Roles {
    ADMIN, MODERATOR, USER
}

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {


    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;
    final EmailService emailService;
    final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        // Check if the user with this email already exists
        if (userAccountRepository.existsById(userRegisterDto.getEmail().trim())) {
            throw new UserExistsException();
        }

        // Convert DTO to Entity and encode the password
        UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
        String password = passwordEncoder.encode(userRegisterDto.getPassword());
        userAccount.setPassword(password);

        // Set default role
        userAccount.addRole(Roles.USER.toString());

        // Save user to database
        userAccountRepository.save(userAccount);

        // Debug: print all users
        userAccountRepository.findAll().forEach(System.out::println);

        // Return mapped UserDto
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto getUser(String email) {
        // Not implemented yet
        return null;
    }

    @Override
    public UserDto removeUser(String email) {
        // Find user by email or throw exception
        UserAccount userAccount = userAccountRepository.findById(email).orElseThrow(UserNotFoundException::new);

        // Delete the user
        userAccountRepository.delete(userAccount);

        // Return deleted user info
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserInfoDto changePassword(String email, String newPassword) {
        // Find user or throw exception
        UserAccount userAccount = userAccountRepository.findById(email).orElseThrow(UserNotFoundException::new);

        // Encode new password and update user
        userAccount.setPassword(passwordEncoder.encode(newPassword));
        userAccountRepository.save(userAccount);

        // Return updated user info
        return modelMapper.map(userAccount, UserInfoDto.class);
    }

    @Override
    public List<UserInfoDto> getAllUsers() {
        // Create result list
        List<UserInfoDto> result = new ArrayList<>();

        // Map each user to DTO and collect to result list
        userAccountRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UserInfoDto.class))
                .forEach(result::add);

        return result;
    }

    @Override
    public void recoveryPassword(String email) {
        // TODO: Replace with secure random password generation
        String passwordDefault = "We_are_the_champions";
        String passwordNew = passwordEncoder.encode(passwordDefault);

        // Find user or throw exception
        UserAccount userAccount = userAccountRepository.findById(email.trim()).orElse(null);
        if (userAccount == null) {
            throw new UserNotFoundException();
        }

        // Update password and save
        userAccount.setPassword(passwordNew);
        userAccountRepository.save(userAccount);

        // Log and send email with new password
        System.out.println("User " + email + " exists");
        emailService.sendEmail(email, "New password for AccessControl app", "Your new password is " + passwordDefault);
    }

    @Override
    public UserDto changeRolesList(String email, String role, boolean isAddRole) {
        // Find user or throw exception
        UserAccount userAccount = userAccountRepository.findById(email.trim()).orElse(null);
        if (userAccount == null) {
            throw new UserNotFoundException();
        }

        // Add or remove role based on flag
        if (isAddRole) {
            userAccount.addRole(role);
        } else {
            userAccount.removeRole(role);
        }

        // Save changes and return updated user
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }
    @Override
	public List<String> getUsersEmail() {
		  return userAccountRepository.findAll().stream()
		            .map(UserAccount::getEmail)
		            .collect(Collectors.toList());
	}
}
