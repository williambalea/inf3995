import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

ApplicationWindow {
    id: applicationWindow
    width: 1280
    height: 960
    visible: true
    color: "#ffffff"
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
            height: 912
            interactive: false
            currentIndex: tabBar.currentIndex

            Page {
                id: surveyPage
                width: applicationWindow.width
                height: applicationWindow.height - 48
                background: Rectangle {
                    color: "#b12c2c"
                }

                Label {
                    id: label
                    x: 0
                    y: 0
                    width: 180
                    height: 57
                    text: qsTr("")
                    horizontalAlignment: Text.AlignHCenter
                    verticalAlignment: Text.AlignVCenter
                    font.pointSize: 22
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
                    anchors.bottomMargin: 40
                    anchors.leftMargin: 31
                    anchors.rightMargin: 37
                    anchors.topMargin: 72
                    model: JSON.parse(backend.sqlData)
                    Component.onCompleted: backend.refresh()

                }
            }

            Page {
                id: engine1Page
                width: applicationWindow.width
                height: applicationWindow.height - 48
                Label {
                    id: label1
                    x: 0
                    y: 0
                    width: 180
                    height: 57
                    text: qsTr("Page 2")
                    horizontalAlignment: Text.AlignHCenter
                    verticalAlignment: Text.AlignVCenter
                    font.pointSize: 22
                }

                TextArea {
                    id: textArea
                    x: 127
                    y: 125
                    width: 1026
                    height: 662
                    verticalAlignment: Text.AlignBottom
                    placeholderText: qsTr("Text Area")
                    background: Rectangle {
                        color: "#ffffff"
                    }
                }

                background: Rectangle {
                    color: "#f36a6a"
                }
            }

            Page {
                id: engine2Page
                width: applicationWindow.width
                height: applicationWindow.height - 48
                Label {
                    id: label2
                    x: 0
                    y: 0
                    width: 180
                    height: 57
                    text: qsTr("Page 3")
                    horizontalAlignment: Text.AlignHCenter
                    verticalAlignment: Text.AlignVCenter
                    font.pointSize: 22
                }

                TextArea {
                    id: textArea1
                    x: 127
                    y: 125
                    width: 1026
                    height: 662
                    verticalAlignment: Text.AlignBottom
                    placeholderText: qsTr("Text Area")
                    background: Rectangle {
                        color: "#ffffff"
                    }
                }

                background: Rectangle {
                    color: "#0c79b7"
                }
            }

            Page {
                id: engine3Page
                width: applicationWindow.width
                height: applicationWindow.height - 48
                Label {
                    id: label3
                    x: 0
                    y: 0
                    width: 180
                    height: 57
                    text: qsTr("Page 4")
                    horizontalAlignment: Text.AlignHCenter
                    verticalAlignment: Text.AlignVCenter
                    font.pointSize: 22
                }

                TextArea {
                    id: textArea2
                    x: 127
                    y: 125
                    width: 1026
                    height: 662
                    verticalAlignment: Text.AlignBottom
                    placeholderText: qsTr("Text Area")
                    background: Rectangle {
                        color: "#ffffff"
                    }
                }

                background: Rectangle {
                    color: "#0c79b7"
                }
            }
        }

    }
}

/*##^##
Designer {
    D{i:0;formeditorZoom:0.5}
}
##^##*/
