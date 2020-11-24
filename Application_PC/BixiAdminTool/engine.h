#ifndef ENGINE_H
#define ENGINE_H

#include <QObject>
#include <QString>
#include <qqml.h>
#include <QNetworkAccessManager>
#include <QNetworkReply>
#include <QTimer>


class Engine : public QObject
{
    Q_OBJECT
    Q_PROPERTY(int idNumber MEMBER id NOTIFY idChanged REQUIRED)
    Q_PROPERTY(QString host MEMBER host NOTIFY hostChanged REQUIRED)
    Q_PROPERTY(QString user MEMBER user NOTIFY userChanged REQUIRED)
    Q_PROPERTY(QString pass MEMBER pass NOTIFY passChanged REQUIRED)
public:
    explicit Engine(QObject *parent = nullptr);
    ~Engine();

signals:
    void idChanged();
    void hostChanged();
    void userChanged();
    void passChanged();
    void logChanged(QString log, bool isText);

private:
    void logsReplyHandler(int code, QByteArray replyRead);
    void setAuthHeader(QNetworkRequest &req, QString user, QString pass);
    QNetworkRequest makeRequest(const QUrl &url);

    QTimer *timer;
    QNetworkAccessManager *man;
    QString host;
    QString user;
    QString pass;
    int id;
    int bytesReceived = 0;

private slots:
    void getLogs();
    void logsFinished(QNetworkReply *reply);

};

#endif // ENGINE_H
