# Task

## Assumptions

1. Application is not secured.
2. In the case of complaint creation and nationality fetching failure I retry the request once. There are many other
   solutions, but it depends on business requirements:
   - Save the complaint without nationality and then fetch asynchronously (queue, async task etc.).
   - Return 5xx.
   - Other configuration with retries and acceptable timeout.
3. I have assumed that there are a large number of complaints, and that they should be returned using paging.