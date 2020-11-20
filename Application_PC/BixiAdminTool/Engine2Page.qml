import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

Page {
    id: engine2Page
    width: applicationWindow.width
    height: applicationWindow.height - 86

    Connections {
        target: backend
        onLog2Changed: {
            var t = "";
            var i = "";
            if(isText) {
                t = log;
            } else {
                var logParts = log.split(" ");
                for (i = 0; i < logParts.length - 1; i++) {
                    t += logParts[i] + " ";
                }

                i = "data:image/png;base64," + logParts[logParts.length - 1];
            }

            elements.insert(0, {
                logText: t,
                image: i,
                typ: isText
            });
        }
    }
    
    Rectangle {
        id: listViewContenant
        x: 5
        y: 5
        width: parent.width - 10
        height: parent.height - 10
        radius: 5
        
        ListView {
            id: listView
            anchors.fill: parent
            spacing: 2
            boundsBehavior: Flickable.StopAtBounds
            clip: true
            ScrollBar.vertical: ScrollBar {}
            model: ListModel {
                id: elements
                
            }
            
            delegate: Rectangle {
                id: rectangle
                width: listViewContenant.width
                height: childrenRect.height
                clip: true
                border.color: "#7b7b7b"
                border.width: 2
                radius: 5
                Text{
                    width: parent.width
                    padding: 5
                    text: logText
                    wrapMode: Text.WordWrap
                }
                Rectangle {
                    id: rectangle1
                    visible: !typ
                    width: typ ? 0 : 500
                    height: typ ? 0 : 500
                    color: "transparent"
                    Image {
                        source: image
                        anchors.leftMargin: 10
                        anchors.bottomMargin: 0
                        anchors.topMargin: 0
                        fillMode: Image.PreserveAspectFit
                        width: 498
                        anchors.left: parent.left
                        anchors.top: parent.top
                        anchors.bottom: parent.bottom
                    }
                }
            }
        }
    }
    
    background: Rectangle {
        color: "#E1E2E1"
    }
}