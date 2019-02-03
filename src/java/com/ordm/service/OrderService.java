/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordm.service;

import com.google.gson.Gson;
import com.ordm.dao.DAOFactory;
import com.ordm.dao.beans.Order;
import com.ordm.dao.OrderDAO;
import com.ordm.exception.ORDMStorageException;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Jul
 */
@Path("order")
public class OrderService {
    static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());

    @Context
    private UriInfo context;    
    
    private final OrderDAO orderDAO = DAOFactory.getOrderDAO();

    /**
     *
     * @param id the id of the order
     * @return A response with the Order in JSON format
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrder(@PathParam("id") String id) {
        try {
            Order order = (Order) orderDAO.get(id);
            
            if (order.getId() != null) {
                Gson gson = new Gson();
                String data = gson.toJson(order); 
                return Response.ok(data, MediaType.APPLICATION_JSON).build();
            }
            else {
                // Would ideally keep messages in a dedicated messaging class with substitutions
                return Response.status(Response.Status.NOT_FOUND).entity("Order not found for id " + id).build();                
            }
                    
        } catch (ORDMStorageException ex) {
            // Would have ideally created an exception filter that would allow throwing 500 errors for all methods
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred: " + ex.getMessage()).build();
        }        
    }
    
    /**
     *
     * @return JSON representation of all orders
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listOrders() {
        try {
            List<Order> orders = orderDAO.list();
            Gson gson = new Gson();
            String data = gson.toJson(orders); 
            return Response.ok(data, MediaType.APPLICATION_JSON).build();
        } catch (ORDMStorageException ex) {            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred: " + ex.getMessage()).build();
        }
    }
    
    /**
     *
     * @param order JSON representation of the order to be created.
     *      Structure: {
     *                   "description" : "description of the order" // this would also contain other Order columns inputed from the UI
     *                 }
     * @return newly created order
     */
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(Order order) {
        try {
            Order createdOrder = (Order) orderDAO.create(order);
            
            Gson gson = new Gson();
            String data = gson.toJson(createdOrder); 
            return Response.ok(data, MediaType.APPLICATION_JSON).build();
        } catch (ORDMStorageException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred: " + ex.getMessage()).build();
        }
    }   
    
    /**
     *
     * @param id id of the order
     * @param order  JSON representation of the order to be updated.
     *      Structure: {
     *                   "description" : "description of the order" // this would also contain other Order columns inputed from the UI
     *                 }
     * @return JSON representation of the updated object
     */
    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateOrder(@PathParam("id") String id, Order order) {        
        try {
            orderDAO.update(id, order);
            return Response.status(Response.Status.OK).build();
        } catch (ORDMStorageException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred: " + ex.getMessage()).build();
        }
    }   
    
    
    /**
     *
     * @param id id of the order
     * @return JSON representation of the updated object
     */
    @POST
    @Path("status/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateOrderStatus(@PathParam("id") String id, Order order) {        
        try {
            orderDAO.forwardStatus(id);
            return Response.status(Response.Status.OK).build();
        } catch (ORDMStorageException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred: " + ex.getMessage()).build();
        }
    }
    
    /**
     *
     * @param id the id of the order
     * @return confirmation that the order has been deleted
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeOrder(@PathParam("id") String id) {       
        try {
            orderDAO.remove(id);
            return Response.status(Response.Status.OK).build();
        } catch (ORDMStorageException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred: " + ex.getMessage()).build();
        }
    }   
}
