import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

Page {
    id: engine3Page
    width: applicationWindow.width
    height: applicationWindow.height - 86

    Engine {
        idNumber: 3
        host: backend.host
        user: backend.user
        pass: backend.pass

        Component.onCompleted: startTimer()

        onLogChanged: {
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
            if (t.length === 0) return;
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
                Text{
                    width: parent.width
                    text: logText
                    wrapMode: Text.WordWrap
                }
                Rectangle {
                    id: rectangle1
                    visible: !typ
                    width: typ ? 0 : parent.width
                    height: typ ? 0 : 500
                    color: "transparent"
                    Image {
                        source: image
                        anchors.leftMargin: 10
                        anchors.bottomMargin: 10
                        anchors.topMargin: 10
                        fillMode: Image.PreserveAspectFit
                        width: parent.width - 10
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

/*##^##
Designer {
    D{i:0;autoSize:true;formeditorZoom:0.5;height:480;width:640}
}
##^##*/
