# aia-test
AIA Test 
The project is done based on AIA Android developer test.Tried to handle almost all the scenarios.

THINGS TO CALL OUT
1. The search results are sorted in reverse chronological order based on the dateTime parameter inside the jsonObject ID while 
	the date time displayed along with the image is from the corresponding image object.
	{
		  "id": "sH1Np4J",
		  "title": "I wish I could focus as well as a cat. ",
		  "description": null,
		  "datetime": 1534633115,
		  "cover": "CdzRzhg",
		  "cover_width": 854,
		  .....
		  ]
		}
2. Pagination is included in the project, but some times we may need to scroll up again to view the latest images from the service call.
   In short there is a small glitch in data loading.
3. Toggle/witch is enabled only when we have a list to display.
4. I have considered all the type of images like, JPG,GIF and MP4
   For videos, I am displaying only the video thumbnail.

LIBRARIES USED
Glide - For image loading
ButterKnife - For UI Binding
Volley - For REST Services
Jackson - JSON Mapping
Espresson - For UI Testing

