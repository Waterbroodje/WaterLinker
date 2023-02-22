
# WaterLinker
WaterLinker provides an easy way for players to link their Minecraft and Discord accounts. Everything is manageable, controllable, and customizable, while also being optimized for server performance.

For more information, and to learn how to use this plugin as an administrator, please visit https://docs.waterbroodje.nl/
## API

WaterLinker offers an extensive API. Although, it's easy to understand. You can get all the data that's being saved in the database from here.

![](https://img.shields.io/badge/version-1.0-blue.svg)

### Creating the WaterLinkerAPI Object
In order to use the API, you need to setup the WaterLinkerAPI. You can do this easily by adding the following to your main class.
```java
WaterLinkerAPI waterLinkerAPI = new WaterLinkerAPI();
```

### Retrieve Data
Now that you've set up the WaterLinkAPI Object, you want to retrieve data. You can do that by getting the `DataGetter` class through the WaterLinkerAPI Object, here's an example:
```java
WaterLinkerAPI waterLinkerAPI = new WaterLinkerAPI();
DataGetter dataGetter = waterLinkerAPI.getDataGetter();
```
Now, you can use the DataGetter to get specific information. Here's an example of how you can get the Discord ID.
```java
WaterLinkerAPI waterLinkerAPI = new WaterLinkerAPI();
DataGetter dataGetter = waterLinkerAPI.getDataGetter();
String userId = dataGetter.getLinkInformation(LinkDataType.DISCORD_ID, player);
// do something with the userId, such as getting the User.
```
Here's a list of all the data you can get using the `getLinkInformation` method:
- Discord ID (Normal ID and Long ID)
- Discord Mention
- Discord Name
- Minecraft Name 
- Minecraft UUID 
- Linking Date

## Looking for more information?
This quick wiki is to show how the basic API works. There are a lot of methods in the API other than the ones shown here. If you are looking for more information on how to set up and use this plugin as an Administrator, or how you can get the maximum out of the API as a Developer, visit https://docs.waterbroodje.nl/
