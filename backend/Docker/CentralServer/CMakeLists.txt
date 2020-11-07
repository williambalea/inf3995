cmake_minimum_required(VERSION 3.0)

project(server)

# Defines
file(GLOB SOURCES 
    "${CMAKE_SOURCE_DIR}/*.cpp")
file(GLOB HEADERS
    "${CMAKE_SOURCE_DIR}/*.hpp"
    "${CMAKE_SOURCE_DIR}/*.h")


# si on veut mettre des makefiles plus tard : add_subdirectory(${CMAKE_SOURCE_DIR}/sio)
add_executable(${PROJECT_NAME} ${SOURCES} ${HEADERS})
target_link_libraries(${PROJECT_NAME} boost_system mysqlcppconn pistache pthread)

#cmake -B out
# dans out/ make