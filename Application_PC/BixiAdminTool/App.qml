import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0


Item {
    Connections{
        target: backend

        onSqlDataChanged: sqlProgBar.visible = false
    }

    TabBar {
        id: tabBar
        x: 0
        y: 50
        width: applicationWindow.width
        height: 0
        currentIndex: 0
        Material.accent: "#ffffff"
        Material.foreground: "#ffffff"


        TabButton {
            id: tab0
            x: 310
            y: 0
            text: qsTr("Surveys")
            Layout.preferredWidth: 150
            background: Rectangle {
                color: "#D52B1E"
            }
        }

        TabButton {
            id: tab1labe
            x: 155
            y: 0
            text: qsTr("Data Engine 1")
            Layout.preferredWidth: 150
            background: Rectangle {
                color: "#D52B1E"
            }
            Rectangle {
                visible: parent.width > 150
                color: backend.engine1Status ?  "#00f715" : "#808080";
                x: (parent.width / 2) - 70
                y: 15
                width: 13
                height: width
                radius: 100
                border.color: "#ffffff"
                border.width: 1
            }
            ToolTip.visible: hovered
            ToolTip.text: qsTr(backend.engine1Status ? "Online" : "Offline")
            ToolTip.delay: 500
            ToolTip.timeout: 2000
        }

        TabButton {
            id: tab2
            x: 0
            y: 0
            text: qsTr("Data Engine 2")
            Layout.preferredWidth: 150
            background: Rectangle {
                color: "#D52B1E"
            }
            Rectangle {
                visible: parent.width > 150
                color: backend.engine2Status ?  "#00f715" : "#808080";
                x: (parent.width / 2) - 70
                y: 15
                width: 13
                height: width
                radius: 100
                border.color: "#ffffff"
                border.width: 1
            }
            ToolTip.visible: hovered
            ToolTip.text: qsTr(backend.engine2Status ? "Online" : "Offline")
            ToolTip.delay: 500
            ToolTip.timeout: 2000
        }

        TabButton {
            id: tab3
            x: 0
            y: 0
            text: qsTr("Data Engine 3")
            Layout.preferredWidth: 150
            background: Rectangle {
                color: "#D52B1E"
            }
            Rectangle {
                visible: parent.width > 150
                color: backend.engine3Status ?  "#00f715" : "#808080";
                x: (parent.width / 2) - 70
                y: 15
                width: 13
                height: width
                radius: 100
                border.color: "#ffffff"
                border.width: 1
            }
            ToolTip.visible: hovered
            ToolTip.text: qsTr(backend.engine3Status ? "Online" : "Offline")
            ToolTip.delay: 500
            ToolTip.timeout: 2000
        }


    }

    SwipeView {
        id: swipeView
        x: 0
        y: 91
        width: 1280
        height: 628
        interactive: false
        currentIndex: tabBar.currentIndex

        Page {
            id: surveyPage
            width: applicationWindow.width
            height: applicationWindow.height - 86
            background: Rectangle {
                color: "#E1E2E1"
            }

            Page3 {
                id: page3
                anchors.fill: parent
                anchors.margins: 6
                anchors.bottomMargin: 10
                model: JSON.parse(backend.sqlData)
                Component.onCompleted: backend.refresh()

            }

            Rectangle {
                z: 1
                anchors.fill: parent
                anchors.margins: 5
                anchors.bottomMargin: 9
                radius: 5
                border.width: 2
                border.color: "#7b7b7b"
                color: "transparent"
            }
            RoundButton {
                id: refreshButton
                x: parent.width - 75
                y: parent.height - 75
                z: 2
                width: 55
                height: 55
                font.pointSize: 40
                display: AbstractButton.IconOnly
                icon.source: "refresh.svg"
                icon.color: "#ffffff"
                icon.width: width/1.5
                icon.height: height/1.5
                Material.background: "#d52b1e"
                Material.foreground: "#ffffff"
                onClicked: {
                    sqlProgBar.visible = true;
                    backend.refresh()
                }
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

    Image {
        id: logo
        x: 8
        y: 0
        width: 117
        height: 50
        source: "logoW.png"
        mipmap: true
        fillMode: Image.PreserveAspectFit
    }

    RoundButton {
        id: roundButton
        x: applicationWindow.width - 50
        y: 0
        width: 50
        height: 50
        font.pointSize: 40
        padding: 0
        topPadding: 0
        font.bold: false
        flat: true
        display: AbstractButton.IconOnly
        icon.source: "settings-24px.svg"
        icon.color: "#ffffff"
        icon.width: width/1.5
        icon.height: height/1.5
        onClicked: stackView.push("Settings.qml")
    }

    ProgressBar {
        id: sqlProgBar
        x: -12
        width: parent.width + 24
        visible: false
        anchors.bottom: parent.bottom
        indeterminate: true
        Material.accent: "#9b0000"
    }

}

/*##^##
Designer {
    D{i:0;autoSize:true;formeditorZoom:0.25;height:480;width:640}
}
##^##*/
