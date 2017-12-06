In this solution I spent quite some time figuring out a way to make it the most efficiently based on the constrains
of the project.

Class CitiesReader has the core method to read the file and search. The parsing was done using Jackson
the developer community seems to agree that is faster than GSON. The reading process is done with pagination
using a pageSize and a Page number. 
The page size is hardcoded in the code to 500 to create a smooth scrolling.
The data structure used to store the cities in a way that they can be ordered by Country then City name is a TreeMap<String, TreeMap<City>>
in this way the order is preverse during insertions and deletions.
The infinite scrooling keeps appending the elements on the list.
The Search was done using the List that is diplayed in the City Fragment. I tried to do it in the JSON but it was extremely slow. I wrapped it around an AsynchTask and the performance
didn't improve significantly.
The searching performance is heavily impacted by the amount of item in the main list. More item more slow it becomes.
To increase the performance I used a Trie implementation for the name of the City in conjunction with AsynchTask.
The search and Read algorithms were validated with several Test Cases using mockito to mock the file reading from assets.