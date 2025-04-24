/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.resources;

/**
 *
 * @author ASUS
 */
import com.bookstore.bookstore.model.Customer;
import com.bookstore.bookstore.service.CustomerService;
import com.bookstore.bookstore.exception.CustomerNotFoundException;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private static final Logger LOGGER = Logger.getLogger(CustomerResource.class.getName());

    @Inject
    CustomerService customerService;

    // POST /customers
    @POST
    public Response createCustomer(@Valid Customer customer) {
        LOGGER.log(Level.INFO, "Creating new customer: {0}", customer.getEmail());
        Customer created = customerService.createCustomer(customer);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    // GET /customers
    @GET
    public List<Customer> getAllCustomers() {
        LOGGER.info("Retrieving all customers");
        return customerService.getAllCustomers();
    }

    // GET /customers/{id}
    @GET
    @Path("/{id}")
    public Customer getCustomerById(@PathParam("id") Integer id) {
        LOGGER.log(Level.INFO, "Retrieving customer with ID: {0}", id);
        return customerService.getCustomerById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    // PUT /customers/{id}
    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Integer id, @Valid Customer customer) {
        LOGGER.log(Level.INFO, "Updating customer with ID: {0}", id);
        Customer updated = customerService.updateCustomer(id, customer);
        return Response.ok(updated).build();
    }

    // DELETE /customers/{id}
    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Integer id) {
        LOGGER.log(Level.INFO, "Deleting customer with ID: {0}", id);
        customerService.deleteCustomer(id);
        return Response.noContent().build();
    }
}
