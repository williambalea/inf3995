#ifndef BACKEND_H
#define BACKEND_H

#include <QObject>
#include <QString>
#include <qqml.h>
#include <QNetworkAccessManager>
#include <QNetworkReply>
#include <map>
#include "engine.h"

#define NB_OF_ENGINES 3

class BackEnd : public QObject
{
    Q_OBJECT
    Q_PROPERTY(QString host MEMBER m_host NOTIFY hostChanged)
    Q_PROPERTY(QString user MEMBER m_user NOTIFY userChanged)
    Q_PROPERTY(QString pass MEMBER m_pass NOTIFY passChanged)
    Q_PROPERTY(QString sqlData READ sqlData NOTIFY sqlDataChanged)
    QML_ELEMENT
public:
    explicit BackEnd(QObject *parent = nullptr);
    ~BackEnd();

    QString sqlData();

    Q_INVOKABLE void refresh();
    Q_INVOKABLE void login(QString user, QString pass);
    Q_INVOKABLE void changePw(QString old, QString newPass);
    Q_INVOKABLE void serverConn(QString host);
    Q_INVOKABLE void startTimer();
    Q_INVOKABLE bool engineStatus(int statusArrayNumber);

signals:
    void hostChanged();
    void userChanged();
    void passChanged();
    void sqlDataChanged();
    void enginesStatusChanged();
    void loginChanged(bool isSuccessful);
    void passwordChanged(bool isSuccessful);
    void serverConnChanged(bool isSuccessful);

private slots:
    void sqlFinished(QNetworkReply *reply);
    void loginFinished(QNetworkReply *reply);
    void checkEnginesFinished(QNetworkReply *reply);
    void changePwFinished(QNetworkReply *reply);
    void serverConnFinished(QNetworkReply *reply);

private:
    void setupNetworkManagers();
    void createNetworkManager(QString manName, void (BackEnd::* slot) (QNetworkReply *));
    QNetworkRequest makeRequest(const QUrl &url);
    void checkEngines();
    void setAuthHeader(QNetworkRequest &req, QString user, QString pass);

    QString m_sqlData;
    QTimer *timer;
    QString m_host;
    QString m_user;
    QString m_pass;
    bool enginesStatus[NB_OF_ENGINES] = {false};
    std::map<QString, QNetworkAccessManager*> mans;
};

#endif // BACKEND_H
