package com.state.machine;

import com.state.machine.review.Events;
import com.state.machine.review.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.statemachine.StateMachine;

/**
 * Created by Dogukan Kundum on 21 Feb 2019.
 */
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class Application implements CommandLineRunner {

	@Autowired
	private StateMachine<States, Events> stateMachine;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	// Provided by CommandLineRunner, will be called just before SpringApplication.run() completes.
	public void run(String... strings) throws Exception {
		// In our case, this is used to indicate that the bean stateMachine, contained within the
		// spring application should be executed the following way.
		stateMachine.sendEvent(Events.E1);
		stateMachine.sendEvent(Events.ENTRY);
		stateMachine.sendEvent(Events.EXIT);
	}
}
