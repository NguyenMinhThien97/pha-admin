package com.store.pharmacy.securities.service;

import com.store.pharmacy.securities.model.UserDTO;

public interface UserService {

	public String save(UserDTO user);
	
	public UserDTO findUser(String userId);
	
	public void checkIfDuplicatedUserName(String userName);
	
	public void checkIfDuplicatedUserEmail(String userName);
	
	public void checkIfDuplicatedUser(UserDTO userDTO);

}
