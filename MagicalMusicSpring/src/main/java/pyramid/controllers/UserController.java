package pyramid.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pyramid.models.Track;
import pyramid.models.User;
import pyramid.repositories.TrackRepository;
import pyramid.repositories.UserRepository;


@CrossOrigin(origins ="http://localhost:4200")	//	Should be the location that the angular server is hosted on
@RestController	//	This is a Web Service
@RequestMapping("/users")
public class UserController {
		
	@Autowired	//	Dependency Injection. Automatic Singleton Instance (from bean?)
	UserRepository userJpa;
	
	@PostMapping("/add")	//	Short-hand for RequestMapping("/add", RequestMethod.POST)
	public ResponseEntity<User> addUser(@RequestBody User user)	//	@RequestBody takes all the info as an object. @RequestParam takes data as pieces
	{
		//	Create a new user if the email is not used
		if(userJpa.findAllByEmail(user.getEmail()).size() == 0)
		{		
			User savedUser = userJpa.save(user);			
			return new ResponseEntity<User>(savedUser, HttpStatus.OK);
		} else	
			return new ResponseEntity<User>(user, HttpStatus.CONFLICT);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<User> getUser(@PathVariable int id)
	{
		Optional<User> returnUser = userJpa.findById(id);
		if(returnUser.isPresent())
		{
			User guy = returnUser.get();	//	Hope this gets the actual user held in Option<User>
			return new ResponseEntity<User>(guy, HttpStatus.OK);
		}
		return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<User>> getUser()
	{
		List<User> allUsers = userJpa.findAll();
		return new ResponseEntity<List<User>>(allUsers, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<User> delete(@PathVariable int id)
	{
		if(userJpa.existsById(id))
		{
			userJpa.deleteById(id);
			return new ResponseEntity<User>(HttpStatus.OK);
		}
		return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	}
		
	@PutMapping("/update/{id}")
	public ResponseEntity<User> update(@PathVariable int id, @RequestParam String name, @RequestParam String password, @RequestParam String image)
	{
		Optional<User> updatedUser = userJpa.findById(id);
		if(updatedUser.isPresent())
		{
			User user = updatedUser.get();
			user.updateValues(name, password, image);	//	No email b/c prompt says you cannot update email
			userJpa.save(user);
			return new ResponseEntity<User>(HttpStatus.OK);
		}
		return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	}
	
	  // Login
    @PostMapping(value = "/login")
    public ResponseEntity<User> login(@RequestParam String username, @RequestParam String password){
    	try
    	{
    		System.out.println("HITTING Login");
	    	List<User> namedUsers = userJpa.findUserByLogin(username, password);
	    	if(namedUsers.size() > 0)
			{
	    		User lUser = namedUsers.get(0);
				System.out.println("Logged In " + lUser.getName() + lUser.getPassword() + lUser.getId());
	    		return new ResponseEntity<User>(lUser, HttpStatus.OK);
			}
    	}
    	catch (Exception e)
    	{
    		System.out.println("Exception Here");
    		System.out.println("YOU PUT THE WRONG INFO");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
    	}
    	return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
//    	System.out.println("Named: " + namedUsers);
//    	for(User namedUser : namedUsers)
//    	{
//    		if(namedUser.getPassword().equals(password))
//    		{
//    			System.out.println("Logged In");
//        		return new ResponseEntity<Track>(HttpStatus.OK);
//    		}
//    	}
    	
    	
    }
}
