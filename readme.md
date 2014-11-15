## Collabolicious

[Link to documentation](https://docs.google.com/document/d/1Li_CskFJnv7puwtEwdcDhoXfn71p0qbEKd6Cqu1yxEg/edit#heading=h.cf8z1kzdb5tn)

1. Todo
    
    ###Harley's suggested order of importance.
    *   Make sure that compilation, running, cleaning, and documentation
    *   generation scripts exist, do what they say, and are functionally the
    *   same across windows and mac.
    *   Clean up all code to spec. This will make doing anything else easier.
    *   Implement anything he required that has not been completed. This one is
    *   obvious.
    *   assets folder should not be in bin or src, should probably be in main
    *   folder (Not a big deal.)
    
	
	### Imperative
	* Networking Code !!!!!!!!!!!!!!!!!!
		* CLEAN UP SERVER CODE !!!!!!!!!!!!!!!
			* Redact unncessary lines and start figuring out a more optimal solution
				* Seriously need to clean this up, column count to high and code is unreadable. No flow.
		* FIX NETWORKMANAGER TO HANDLE MULTIPLE WHITEBOARD STATES, AND MULTIPLE WHITEBOARDS AT THE SAME TIME
		* Implement ENUM VERBAGE, not integers
		* Change Protocal Verbum
		* Functions that need to be implemeneted
			- GET USERS {Returns all users online}
			- GET USER {username} (Returns all whiteboards)
			- GET WHITEBOARD {id} (Returns selected whiteboard)
			- DELETE WHITEBOARD {id} (Deletes the selected whiteboard)
			- UPDATE WHITEBOARD {id} {VERB (PUT/DELETE/UPDATE)} {MODELNAME} {MODELATTRIBUTES} 
			(Updates the the whiteboard with the specific verb with the new model and its attributes)
	* When sharing a whiteboard with a user, use GET USERS to show all available users

	## Secondary
	* Finish/Integrate the backend and frontend for making edits to existing objects on whiteboards
	* Be able to serialize whiteboard object to local cache
	* Put all actions into a queue and when control + z or command + z it will start to do the opposite to the things on the queue to remove them one by one
	* When typing without / with a tool selected it will make a textbox with that text
	* Implement LAN Detection
	* implement RSA encription - partially completed, now need to integrate with existing code
	* Network functions should also include local discovery !!!
	* Nothing implements serializable... !!!
	* Must be able to import IO into saving file to local computer !!!

2. Completed

	* Go over NClass Diagram with a more optimal approach
	* Split whiteboard views into other classes
	* Follow more strict MVC Patterns to enhance code reusability
	* Combine all documentation into Google DOC
	* Create a more modular saving structure for the whiteboard model wether it is local or on the network.
		* Create a LocalWhiteboardModel  
		* Create a NetworkWhiteboardModel
	* Implement GUI ToolMarkerView
	* Implement GUI ToolEraserView
	* Implement WhiteboardPane.java
		- This will basically handle all of the drawing, updating and adding of objects
	* Implement GUI WhiteboardView
	* Implement GUI LoginView
	* Implement GUI WhiteboardSelection
	* Implement GUI WhiteboardSelectionSidebarView
	* Implement GUI WhiteboardSelectionCollectionView
	* Implement GUI WhiteboardSelectionItemView
	* Figure out how to use layeredPane to allow positioning of movable objects.
	* ImageModel
		* ImageView
			* Implement GUI ToolImageView
	* WebpageModel
		* WebpageView
			* Implement GUI ToolWebpageView
	* TextboxModel
		* TextboxView
			* Implement GUI ToolTextView
	* Be able to listen to key strokes in WhiteboardPane
	* Be able to delete a component using the delete key
	* Updated GUI sidebarUI
	* Updated GUI toolbarPanel
	* Updated Marker tool gui
	* Updated Eraser tool gui
	* Updated Text tool gui
	* Update Link Tool Gui
	* Update Image Tool Gui
	* Change design of whiteboard to incorporate thoughts
	* Shape
		* ShapeView
			* Implement GUI ToolShapeView
	### Imperative
	* FileModel
		* FileView
			* Implement GUI ToolFileView
	* Update all GUI to have unique look

3. In Progress

