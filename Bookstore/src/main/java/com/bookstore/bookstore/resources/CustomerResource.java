/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.resources;

import com.bookstore.bookstore.exception.CustomerNotFoundException;
import com.bookstore.bookstore.exception.InvalidInputException;
import com.bookstore.bookstore.model.Customer;
import com.bookstore.bookstore.service.CustomerService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private static final Logger logger = Logger.getLogger(CustomerResource.class.getName());
    private final CustomerService customerService = new CustomerService();

    // Email regex pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");

    @POST
    public Response addCustomer(@Valid Customer customer, @Context UriInfo uriInfo) {
        logger.info("POST /customers - Adding new customer");

        validateCustomer(customer);
        validatePassword(customer.getPassword());

        Customer created = customerService.addCustomer(customer);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(created.getId()));
        logger.log(Level.INFO, "Customer created with ID: {0}", created.getId());
        return Response.created(builder.build()).entity(created).build();
    }

    @GET
    public Response getAllCustomers() {
        logger.info("GET /customers - Retrieving all customers");
        List<Customer> customers = customerService.getAllCustomers();
        return Response.ok(customers).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") int id) {
        logger.log(Level.INFO, "GET /customers/{0} - Retrieving customer by ID", id);
        return customerService.getCustomerById(id)
                .map(customer -> Response.ok(customer).build())
                .orElseThrow(() -> {
                    logger.log(Level.WARNING, "Customer with ID {0} not found", id);
                    throw new CustomerNotFoundException("Invalid Customer ID: " + id);
                });
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, @Valid Customer customer) {
        logger.log(Level.INFO, "PUT /customers/{0} - Updating customer", id);

        if (customer == null) {
            logger.warning("Received null Customer object in PUT request");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", "Customer data must not be null."))
                    .build();
        }

        // Fetch existing customer
        Customer existingCustomer = customerService.getCustomerById(id)
                .orElseThrow(() -> {
                    logger.log(Level.WARNING, "Customer with ID {0} not found for update", id);
                    return new CustomerNotFoundException(id);
                });

        validateCustomer(customer);

        if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
            // Keep existing password if not provided
            customer.setPassword(existingCustomer.getPassword());
        } else {
            // Validate new password if provided
            validatePassword(customer.getPassword());
        }

        return customerService.updateCustomer(id, customer)
                .map(updated -> {
                    logger.log(Level.INFO, "Customer updated with ID: {0}", id);
                    return Response.ok(updated).build();
                })
                .orElseThrow(() -> {
                    logger.log(Level.WARNING, "Customer with ID {0} not found for update", id);
                    return new CustomerNotFoundException("Customer not found with ID: " + id);
                });
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        logger.log(Level.INFO, "DELETE /customers/{0} - Deleting customer", id);

        if (customerService.deleteCustomer(id)) {
            logger.log(Level.INFO, "Customer deleted with ID: {0}", id);
            String successMessage = String.format("Customer with ID %d deleted successfully", id);
            return Response.ok(Collections.singletonMap("message", successMessage)).build();
        } else {
            logger.log(Level.WARNING, "Customer with ID {0} not found for deletion", id);
            throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }
    }

    private void validateCustomer(Customer customer) {
        // Validate First Name and Last Name
        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()
                || !customer.getFirstName().matches("^[a-zA-Z]+$")) {
            logger.warning("Invalid first name detected");
            throw new InvalidInputException("First name must be alphabetic and not null.");
        }

        if (customer.getLastName() == null || customer.getLastName().trim().isEmpty()
                || !customer.getLastName().matches("^[a-zA-Z]+$")) {
            logger.warning("Invalid last name detected");
            throw new InvalidInputException("Last name must be alphabetic and not null.");
        }

        // Validate Email
        if (customer.getEmail() == null || !EMAIL_PATTERN.matcher(customer.getEmail()).matches()) {
            logger.warning("Invalid email detected");
            throw new InvalidInputException("Invalid email format.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().length() < 5) {
            logger.warning("Invalid password detected");
            throw new InvalidInputException("Password must be at least 5 characters long.");
        }
    }
}




