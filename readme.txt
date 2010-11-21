thoughts:
 - there are two scenarios. one is that we can register beans automatically to be dispatched via the SI channel interface
  using something like:

   @ActivitiHandler
   class MyHandler {
     @ActivitiState( "confirm-receipt")
     public void confirm ( @ProcessVariables("customerId") long customerId ){
        // ...
     }
   }

   - the other is that we can have the invocation of a bean method trigger the claiming of a task to a given user or perhaps the creatin of a
   business process... im not sure how this might look.

   interface .. {
   @ActivitiStart ( "customer-signup-process")
   void signupCustomer ( @ProcessVariable("customerId") long customerId);
   }

   or

   class .. {
     @ActivitiStart ("customer-signup-process")
     public void signupCustomer( @ProcessVariable("customerId") long customerId){
       // your customer logic here to setup state before invocation of the process
     }
    }

    // or, we could also let them execute logic themselves (optionally feeding into the started process
    // on their behalf by providing a map of process variables

    class ... {
     @Autowired private CustomerService pizzaShop;

     @ActivitiStart ("customer-signup-process")
     public Map<String,Object> signupCustomer(  String fn, String ln){
       // your customer logic here to setup state before invocation of the process
       Customer customer = pizzaShop.createCustomer( fn, ln);
       Map<String,Object> mapOfProcVars = new HashMap<String,Object>();
       mapOfProcVars.put("customerId", customer.getId());
       return mapOfProcVars ;
     }
    }

   long customerId = 102 ;
   myHandler.signupCustomer(customerId);