/*
    ===================================================PROIECT PG==========================================================

    REALIZAT DE: Fărcaș Tudor Ioan
    GRUPA: 30235, CTI SERIA B

    ===================================================PROIECT PG==========================================================
*/
#if defined (__APPLE__)
    #define GLFW_INCLUDE_GLCOREARB
    #define GL_SILENCE_DEPRECATION
#else
    #define GLEW_STATIC
    #include <GL/glew.h>
#endif

#include <GLFW/glfw3.h>

#include <glm/glm.hpp> //core glm functionality
#include <glm/gtc/matrix_transform.hpp> //glm extension for generating common transformation matrices
#include <glm/gtc/matrix_inverse.hpp> //glm extension for computing inverse matrices
#include <glm/gtc/type_ptr.hpp> //glm extension for accessing the internal data structure of glm types



/*    ==================================DO NOT DECOMMENT THIS PART==================================
#include <OpenAL/al.h>
#include <OpenAL/alc.h>
*/   // =================================THANK YOU==================================================


#include "Window.h"
#include "Shader.hpp"
#include "Camera.hpp"
#include "Model3D.hpp"
#include "SkyBox.hpp"

#include <iostream>

// window
gps::Window myWindow;

// matrices
glm::mat4 model;

glm::mat4 audi_model;
glm::mat3 audi_normalMatrix;
glm::mat4 audi_view;

glm::mat4 view;
glm::mat4 projection;
glm::mat3 normalMatrix;

// light parameters
glm::vec3 lightDir;
glm::vec3 lightColor;

// shader uniform locations
GLint modelLoc;

GLint viewLoc;
GLint projectionLoc;
GLint normalMatrixLoc;

GLint lightDirLoc;
GLint lightColorLoc;
GLint timeLoc;

GLboolean fogLoc;
GLboolean rainLoc;
GLboolean snowLoc;
GLboolean nightLoc;
GLboolean shadowLoc;
GLboolean audi_modelLoc;

// camera
gps::Camera myCamera(
    glm::vec3(0.0f, 2.0f, 2.5f),
    glm::vec3(0.0f, 0.0f, -10.0f),
    glm::vec3(0.0f, 1.0f, 0.0f));



GLfloat cameraSpeed = 0.1f;
GLfloat lightAngle;

GLboolean pressedKeys[1024];

// models
gps::Model3D teapot;

gps::Model3D demopg;
gps::Model3D audi;


GLfloat angle;
GLfloat audi_angle;


glm::mat4 lightRotation;

// skybox
gps::SkyBox mySkyBox;
std::vector<const GLchar*> faces;



// shaders
gps::Shader myBasicShader;
gps::Shader skyboxShader;

//=================================================INITIALIZARI=========================================================================


void initModels() {
   // teapot.LoadModel("models/teapot/teapot20segUT.obj");
    demopg.LoadModel("models/demo/pg.obj");
    audi.LoadModel("models/demo/audi.obj");
}

void initShaders() {
	myBasicShader.loadShader(
        "shaders/basic.vert",
        "shaders/basic.frag");

   
}


void initSkyBoxShader()
{
    mySkyBox.Load(faces);
    skyboxShader.loadShader("shaders/skybox.vert", "shaders/skybox.frag");
    skyboxShader.useShaderProgram();
    view = myCamera.getViewMatrix();
    glUniformMatrix4fv(glGetUniformLocation(skyboxShader.shaderProgram, "view"), 1, GL_FALSE,
        glm::value_ptr(view));

    projection = glm::perspective(glm::radians(45.0f), (float)myWindow.getWindowDimensions().width / (float)myWindow.getWindowDimensions().height, 0.1f, 1000.0f);
    glUniformMatrix4fv(glGetUniformLocation(skyboxShader.shaderProgram, "projection"), 1, GL_FALSE,
        glm::value_ptr(projection));
} 

/*      ==================================DO NOT DECOMMENT THIS PART==================================

// OpenAL variables
ALCdevice* device = nullptr;
ALCcontext* context = nullptr;


void initOpenAL() {
    device = alcOpenDevice(nullptr); // Open the default audio device
    if (!device) {
        std::cerr << "Failed to open OpenAL device" << std::endl;
        // Handle the error, maybe exit the application
        return;
    }

    context = alcCreateContext(device, nullptr);
    if (!context) {
        std::cerr << "Failed to create OpenAL context" << std::endl;
        alcCloseDevice(device);
        // Handle the error, maybe exit the application
        return;
    }

    alcMakeContextCurrent(context);
    // At this point, OpenAL is initialized and ready to use
}

void cleanupOpenAL() {
    alcMakeContextCurrent(nullptr);
    alcDestroyContext(context);
    alcCloseDevice(device);
}
*/  // =================================THANK YOU==================================================

GLenum glCheckError_(const char *file, int line)
{
	GLenum errorCode;
	while ((errorCode = glGetError()) != GL_NO_ERROR) {
		std::string error;
		switch (errorCode) {
            case GL_INVALID_ENUM:
                error = "INVALID_ENUM";
                break;
            case GL_INVALID_VALUE:
                error = "INVALID_VALUE";
                break;
            case GL_INVALID_OPERATION:
                error = "INVALID_OPERATION";
                break;
            case GL_OUT_OF_MEMORY:
                error = "OUT_OF_MEMORY";
                break;
            case GL_INVALID_FRAMEBUFFER_OPERATION:
                error = "INVALID_FRAMEBUFFER_OPERATION";
                break;
        }
		std::cout << error << " | " << file << " (" << line << ")" << std::endl;
	}
	return errorCode;
}

#define glCheckError() glCheckError_(__FILE__, __LINE__)

void windowResizeCallback(GLFWwindow* window, int width, int height) {
	fprintf(stdout, "Window resized! New width: %d , and height: %d\n", width, height);
	//TODO
    WindowDimensions resize{};
    resize.width = width;
    resize.height = height;
    myWindow.setWindowDimensions(resize);
}


void keyboardCallback(GLFWwindow* window, int key, int scancode, int action, int mode) {
	if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
        glfwSetWindowShouldClose(window, GL_TRUE);
    }


	if (key >= 0 && key < 1024) {
        if (action == GLFW_PRESS) {
            pressedKeys[key] = true;
        } else if (action == GLFW_RELEASE) {
            pressedKeys[key] = false;
        }
    }

}


//mouseCallBack variables

float sensitivity = 0.1f;
float pitch = 0.0f, yaw = 0.0f;
bool firstMouse = true;

float lastX = 0.0f, lastY = 0.0f;

void mouseCallback(GLFWwindow* window, double xpos, double ypos) {
    if (firstMouse) {
        lastX = static_cast<float>(xpos);
        lastY = static_cast<float>(ypos);
        firstMouse = false;
    }

    float xoffset = static_cast<float>(xpos - lastX) * sensitivity;
    float yoffset = static_cast<float>(lastY - ypos) * sensitivity;

    lastX = static_cast<float>(xpos);
    lastY = static_cast<float>(ypos);

    yaw += xoffset;
    pitch += yoffset;

    // Clamp pitch to prevent flipping
    pitch = glm::clamp(pitch, -90.0f, 90.0f);

    myCamera.rotate(pitch, yaw);
    myCamera.rotate(pitch, yaw);
    view = myCamera.getViewMatrix();
    myBasicShader.useShaderProgram();
    glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
    // compute normal matrix for teapot
    normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
}

void processMovement() {
	
// ===================== CAMERA MOVEMENT =====================


    if (pressedKeys[GLFW_KEY_W]) {
		myCamera.move(gps::MOVE_FORWARD, cameraSpeed);
		//update view matrix
        view = myCamera.getViewMatrix();
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot
        normalMatrix = glm::mat3(glm::inverseTranspose(view*model));
	}

	if (pressedKeys[GLFW_KEY_S]) {
		myCamera.move(gps::MOVE_BACKWARD, cameraSpeed);
        //update view matrix
        view = myCamera.getViewMatrix();
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot
        normalMatrix = glm::mat3(glm::inverseTranspose(view*model));
	}

	if (pressedKeys[GLFW_KEY_A] || pressedKeys[GLFW_KEY_LEFT]) {
		myCamera.move(gps::MOVE_LEFT, cameraSpeed);
        //update view matrix
        view = myCamera.getViewMatrix();
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot
        normalMatrix = glm::mat3(glm::inverseTranspose(view*model));
	}

	if (pressedKeys[GLFW_KEY_D] || pressedKeys[GLFW_KEY_RIGHT]) {
		myCamera.move(gps::MOVE_RIGHT, cameraSpeed);
        //update view matrix
        view = myCamera.getViewMatrix();
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot
        normalMatrix = glm::mat3(glm::inverseTranspose(view*model));
	}

    if (pressedKeys[GLFW_KEY_UP]) {
        myCamera.move(gps::MOVE_UP, cameraSpeed);
        //update view matrix
        view = myCamera.getViewMatrix();
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot
        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
    }

    if (pressedKeys[GLFW_KEY_DOWN]) {
        myCamera.move(gps::MOVE_DOWN, cameraSpeed);
        //update view matrix
        view = myCamera.getViewMatrix();
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot
        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
    }

     if (pressedKeys[GLFW_KEY_Q]) {
        angle -= 1.0f;
        // update model matrix for teapot
        model = glm::rotate(glm::mat4(1.0f), glm::radians(angle), glm::vec3(0, 1, 0));
        // update normal matrix for teapot
        normalMatrix = glm::mat3(glm::inverseTranspose(view*model));
    }

    if (pressedKeys[GLFW_KEY_E]) {
        angle += 1.0f;
        // update model matrix for teapot
        model = glm::rotate(glm::mat4(1.0f), glm::radians(angle), glm::vec3(0, 1, 0));
        // update normal matrix for teapot
        normalMatrix = glm::mat3(glm::inverseTranspose(view*model));
    }

    if(pressedKeys[GLFW_KEY_KP_ADD]){
        cameraSpeed += 0.1f;
    }

    if(pressedKeys[GLFW_KEY_KP_SUBTRACT]){
        cameraSpeed -= 0.1f;
    }

    //for audi car
//==========================================================================================================================

    /*
    if (pressedKeys[GLFW_KEY_Z]) {
        audi_angle -= 1.0f;
        // update model matrix for teapot
        audi_model = glm::rotate(glm::mat4(1.0f), glm::radians(audi_angle), glm::vec3(0, 1, 0));
        // update normal matrix for teapot
        audi_normalMatrix = glm::mat3(glm::inverseTranspose(audi_view*audi_model));
    }

    if (pressedKeys[GLFW_KEY_X]) {
        audi_angle += 1.0f;
        // update model matrix for teapot
        audi_model = glm::rotate(glm::mat4(1.0f), glm::radians(audi_angle), glm::vec3(0, 1, 0));
        // update normal matrix for teapot
        audi_normalMatrix = glm::mat3(glm::inverseTranspose(audi_view*audi_model));
    }
*/    


    if(pressedKeys[GLFW_KEY_Z]){
        myBasicShader.useShaderProgram();
        glUniform1i(audi_modelLoc, true);
    }

    if(pressedKeys[GLFW_KEY_X]){
        myBasicShader.useShaderProgram();
        glUniform1i(audi_modelLoc, false);
    }


//==========================================================================================================================


    //rain (sunt noises asa ca trebuie sa stai aproape sa vezi ceva)
     if (pressedKeys[GLFW_KEY_J]) {
        myBasicShader.useShaderProgram();
        glUniform1i(rainLoc, true);
    }

     if (pressedKeys[GLFW_KEY_K]) {
        myBasicShader.useShaderProgram();
        glUniform1i(rainLoc, false);
    }



    //snow(aici somehow am reusit sa fac sa se vada mai bine, idk cum)
    if(pressedKeys[GLFW_KEY_O]){
        myBasicShader.useShaderProgram();
        glUniform1i(snowLoc, true);
    }

    if(pressedKeys[GLFW_KEY_P]){
        myBasicShader.useShaderProgram();
        glUniform1i(snowLoc, false);
    }

  

    //fog

    if (pressedKeys[GLFW_KEY_N]) {
        myBasicShader.useShaderProgram();
        glUniform1i(fogLoc, true);
    }

    if (pressedKeys[GLFW_KEY_M]) {
        myBasicShader.useShaderProgram();
        glUniform1i(fogLoc, false);
    }

    //night
    if (pressedKeys[GLFW_KEY_B]) {
        myBasicShader.useShaderProgram();
        glUniform1i(nightLoc, true);
    }
    if(pressedKeys[GLFW_KEY_V]){
        myBasicShader.useShaderProgram();
        glUniform1i(nightLoc, false);
    }

    //shadow (nu merge!!!!)
/*
    if(pressedKeys[GLFW_KEY_1]){
        myBasicShader.useShaderProgram();
        glUniform1i(shadowLoc, true);
    }

    if(pressedKeys[GLFW_KEY_2]){
        myBasicShader.useShaderProgram();
        glUniform1i(shadowLoc, false);
    }
*/

//==========================================================================================================================

      // wireframe
    if (pressedKeys[GLFW_KEY_1]) {
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }
    // poly
    if (pressedKeys[GLFW_KEY_2]) {
        glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);
    }
    // normal
    if (pressedKeys[GLFW_KEY_3]) {
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }
   


}


void initOpenGLWindow() {
    myWindow.Create(1024, 768, "OpenGL Project Core");
       
}

void initFaces(){
    faces.push_back("skybox/nightsky/nightsky_ft.tga");
    faces.push_back("skybox/nightsky/nightsky_bk.tga");
    faces.push_back("skybox/nightsky/nightsky_up.tga");
    faces.push_back("skybox/nightsky/nightsky_dn.tga");
    faces.push_back("skybox/nightsky/nightsky_rt.tga");
    faces.push_back("skybox/nightsky/nightsky_lf.tga");
} 

void setWindowCallbacks() {
	glfwSetWindowSizeCallback(myWindow.getWindow(), windowResizeCallback);
    glfwSetKeyCallback(myWindow.getWindow(), keyboardCallback);
    glfwSetCursorPosCallback(myWindow.getWindow(), mouseCallback);
   
}

void initOpenGLState() {
	glClearColor(0.7f, 0.7f, 0.7f, 1.0f);
	glViewport(0, 0, myWindow.getWindowDimensions().width, myWindow.getWindowDimensions().height);
    glEnable(GL_FRAMEBUFFER_SRGB);
	glEnable(GL_DEPTH_TEST); // enable depth-testing
	glDepthFunc(GL_LESS); // depth-testing interprets a smaller value as "closer"
	glEnable(GL_CULL_FACE); // cull face
	glCullFace(GL_BACK); // cull back face
	glFrontFace(GL_CCW); // GL_CCW for counter clock-wise
}





void initUniforms() {
	myBasicShader.useShaderProgram();
    

    // create model matrix for teapot
    model = glm::rotate(glm::mat4(1.0f), glm::radians(angle), glm::vec3(0.0f, 1.0f, 0.0f));
   // audi_model = glm::rotate(glm::mat4(1.0f), glm::radians(audi_angle), glm::vec3(0.0f, 1.0f, 0.0f));
	modelLoc = glGetUniformLocation(myBasicShader.shaderProgram, "model");

	// get view matrix for current camera
	view = myCamera.getViewMatrix();
	viewLoc = glGetUniformLocation(myBasicShader.shaderProgram, "view");
	// send view matrix to shader
    glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));

    // compute normal matrix for teapot
    normalMatrix = glm::mat3(glm::inverseTranspose(view*model));
   // audi_normalMatrix = glm::mat3(glm::inverseTranspose(view*audi_model));
	normalMatrixLoc = glGetUniformLocation(myBasicShader.shaderProgram, "normalMatrix");

	// create projection matrix
	projection = glm::perspective(glm::radians(45.0f),
                               (float)myWindow.getWindowDimensions().width / (float)myWindow.getWindowDimensions().height,
                               0.1f, 20.0f);
	projectionLoc = glGetUniformLocation(myBasicShader.shaderProgram, "projection");
	
    // send projection matrix to shader
	glUniformMatrix4fv(projectionLoc, 1, GL_FALSE, glm::value_ptr(projection));	

	//set the light direction (direction towards the light)
	lightDir = glm::vec3(0.0f, 1.0f, 1.0f);
	lightDirLoc = glGetUniformLocation(myBasicShader.shaderProgram, "lightDir");
	
    // send light dir to shader
	glUniform3fv(lightDirLoc, 1, glm::value_ptr(lightDir));

	//set light color
	lightColor = glm::vec3(1.2f, 1.2f, 1.2f); //white light
	lightColorLoc = glGetUniformLocation(myBasicShader.shaderProgram, "lightColor");
	// send light color to shader
	glUniform3fv(lightColorLoc, 1, glm::value_ptr(lightColor));

    //pentru ceata
    fogLoc= glGetUniformLocation(myBasicShader.shaderProgram, "showFog");
    glUniform1i(fogLoc, false);

    //pentru ploaie
   rainLoc= glGetUniformLocation(myBasicShader.shaderProgram, "showRain");
    glUniform1i(rainLoc, false);
    float currentTime = glfwGetTime();
    
    //pentru timp
    timeLoc = glGetUniformLocation(myBasicShader.shaderProgram, "time");
    glUniform1f(timeLoc, currentTime);


    //pentru zapada
     snowLoc= glGetUniformLocation(myBasicShader.shaderProgram, "showSnow");
     glUniform1i(snowLoc, false);

     //pentru noapte
        nightLoc= glGetUniformLocation(myBasicShader.shaderProgram, "showNight");
        glUniform1i(nightLoc, false);

        //pentru umbra
        shadowLoc= glGetUniformLocation(myBasicShader.shaderProgram, "showShadow");
        glUniform1i(shadowLoc, false);

        audi_modelLoc = glGetUniformLocation(myBasicShader.shaderProgram, "showSpotLight");
        glUniform1i(audi_modelLoc, false);
}

void renderShader(gps::Shader shader) {
   
    // select active shader program
    shader.useShaderProgram();


    //send object model matrix data to shader
    glUniformMatrix4fv(modelLoc, 1, GL_FALSE, glm::value_ptr(model));

    //send object normal matrix data to shader
    glUniformMatrix3fv(normalMatrixLoc, 1, GL_FALSE, glm::value_ptr(normalMatrix));

    // draw objects
    //teapot.Draw(shader);
   demopg.Draw(shader);
   audi.Draw(shader);

    
}


void renderScene() {
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	

	// render the scene
	renderShader(myBasicShader);
    
    //render skybox
    mySkyBox.Draw(skyboxShader, view, projection);

}

void cleanup() {
    myWindow.Delete();
    //cleanup code for your own data
}

int main(int argc, const char * argv[]) {

    try {
        initOpenGLWindow();
    } catch (const std::exception& e) {
        std::cerr << e.what() << std::endl;
        return EXIT_FAILURE;
    }

    initOpenGLState();
    initFaces();
	initModels();

	initShaders();
    initSkyBoxShader();
	
    initUniforms();
    setWindowCallbacks();

   //  initOpenAL();  // Initialize OpenAL

	glCheckError();
	// application loop
	while (!glfwWindowShouldClose(myWindow.getWindow())) {
        processMovement();
	    renderScene();

		glfwPollEvents();
		glfwSwapBuffers(myWindow.getWindow());

		glCheckError();
	}

    // cleanupOpenAL();  // Cleanup OpenAL
	cleanup();

    return EXIT_SUCCESS;
}
