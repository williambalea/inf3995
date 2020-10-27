import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

ApplicationWindow {
    id: applicationWindow
    width: 1280
    height: 720
    visible: true
    title: qsTr("Bixi Admin Tool")

    BackEnd {
        id: backend
    }

    Item {
        id: login
        visible: !backend.isLoggedIn

        LoginPage {
            id: loginPage
        }

    }

    Item {
        id: app
        visible: backend.isLoggedIn

        TabBar {
            id: tabBar
            x: 0
            y: 0
            width: applicationWindow.width
            height: 0
            currentIndex: 0

            TabButton {
                id: tab0
                x: 310
                y: 0
                height: 48
                text: qsTr("Surveys")
                Layout.preferredWidth: 150
            }

            TabButton {
                id: tab1
                x: 155
                y: 0
                text: qsTr("Data Engine 1")
                Layout.preferredWidth: 150
            }

            TabButton {
                id: tab2
                x: 0
                y: 0
                text: qsTr("Data Engine 2")
                Layout.preferredWidth: 150
            }

            TabButton {
                id: tab3
                x: 0
                y: 0
                text: qsTr("Data Engine 3")
                Layout.preferredWidth: 150
            }


        }

        SwipeView {
            id: swipeView
            x: 0
            y: 48
            width: 1280
            height: 676
            interactive: false
            currentIndex: tabBar.currentIndex

            Page {
                id: surveyPage
                width: applicationWindow.width
                height: applicationWindow.height - 48
                background: Rectangle {
                    color: "#be3d34"
                }

                Button {
                    id: refreshButton
                    x: 1052
                    y: 9
                    width: 191
                    height: 48
                    text: qsTr("Refresh")

                    Connections {
                        target: refreshButton
                        onClicked: function () {
                            print("from qml");
                            backend.refresh();
                            print(backend.sqlData);
                        }
                    }
                }

                Page3 {
                    id: page3
                    x: 56
                    y: 558
                    width: 512
                    height: 200
                    anchors.bottomMargin: 36
                    anchors.leftMargin: 31
                    anchors.rightMargin: 37
                    anchors.topMargin: 72
                    model: JSON.parse(backend.sqlData)
                    Component.onCompleted: backend.refresh()

                }
            }

            Engine1Page {
                id: engine1Page
            }

            Engine1Page {
                id: engine1Page1
            }

            Engine1Page {
                id: engine1Page2
            }
        }

    }
}

/*##^##
Designer {
    D{i:0;formeditorZoom:0.5}
}
##^##*/
