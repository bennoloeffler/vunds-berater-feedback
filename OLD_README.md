# Feedback für V&S-Berater

## hosted here
https://bennoloeffler.github.io/vunds-berater-feedback/  
connects to decision-konsent to send mails!

## usage
Just send this link:  
https://bennoloeffler.github.io/vunds-berater-feedback/


THIS SHOULD WORK, BUT DOES NOT YET!  
in order to pre-configure a consultants name, use a link like this:  
https://bennoloeffler.github.io/vunds-berater-feedback/?consultant=Benno%20Löffler

## dev

```shell
#create pom
shadow-cljs pom
#open that one in intellij
```

to start the compiler:
```shell
npm run watch
```

open browser at http://localhost:8280

look here for starting repl:  
https://shadow-cljs.github.io/docs/UsersGuide.html#_cursive

## deploy
first of all, compile in production mode:
```shell
npm run release
```
then copy app.js to /docs
and maybe all css and pictures, if updated.

then deploy especially /docs to github
