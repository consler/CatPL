# CatPL
**Easy to use programming language, designed to make cross-platform graphical applications for Windows, Linux, Mac, BSD and Android.**

### Alpha features:
- [x] Basic arithmetics — +, -, *, /
- [x] Variables — assignment and usage
- [x] Indentation check
- [x] Auto float conversion
- [ ] Events:
- -  [x] OnStart
- -  [ ] OnClick
- [x] If statements
- [ ] While loops
- [ ] Tables
- [ ] Individual scenes
- [ ] Graphical elements
- [ ] Functions

### Example syntax:
```
onStart:
    x = 7
    y = 12 + 4/2
    log("hmm what could be the answer??")

onStart:
    z=10
    log("not "+ z)
    answer = x / y
    log("answer is " + answer)
```