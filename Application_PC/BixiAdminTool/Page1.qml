import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0

Page {
    id: page1
    width: 200
    height: 200
    Label {
        id: label2
        x: 0
        y: 0
        width: 180
        height: 57
        text: qsTr("Page 1")
        horizontalAlignment: Text.AlignHCenter
        verticalAlignment: Text.AlignVCenter
        font.pointSize: 22
    }
    background: Rectangle {
        color: "#f3bf6a"
        border.color: "#1a2398"
    }
}
