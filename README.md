# Task

## Assumptions

1. Application is not secured.
2. In the case of complaint creation and nationality fetching failure I return 5xx. There are many other
   better solutions, but it depends on business requirements:
   - Retry call (resilience4j, spring-retry, manually in code)
   - Other configuration with retries and acceptable timeout.
   - Save the complaint without nationality and then fetch asynchronously (queue, async task etc.).
3. I have assumed that there are a large number of complaints, and that they should be returned using paging.
4. I save instant date without offset.
5. IP is passed in 'X-Forwarded-For' header, more details in: com.example.task.controller.ComplaintController.
6. I've chosen https://ip-api.com/docs/api:json as free IP API service .
7. There aren't true integration tests because I use a mock mvc and mock some dependencies like a 'clock'. 
8. I added db asserts in tests to indicate that I met requirements. There are different approaches to test applications
   and one is to test only via API (true integration tests) but I decided to show that everything is correctly saved in
   db.
