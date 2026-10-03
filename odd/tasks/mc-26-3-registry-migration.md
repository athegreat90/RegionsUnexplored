# Feature: Finish the Minecraft 26.3 registry migration

## Objective

Finish migrating the four sibling worldgen registries (TreeDecorator, RootPlacer,
TrunkPlacer, FoliagePlacer) to the MC 26.3 codec()-based registry shape that commit
`82127afd` already applied to `Feature` and `BlockStateProvider`, then get the project
compiling and booting on both loaders again.

## Why

MC 26.2/26.3 collapsed `Xxx` + `XxxType<T>` registration pairs into a single
self-contained interface exposing `codec()` instead of a static `TYPE` field +
`type()` override. Branch `26.3-indev` only did this for `Feature`/`BlockStateProvider`
so far; the four sibling registries are the next blocker before the project compiles
clean on MC 26.3.

## Problem / scope

Full plan: `/home/athegreat90/.claude/plans/mutable-tickling-coral.md` (approved).

- Work Unit 1: migrate `RUTreeDecoratorTypes.java`, `RURootPlacerTypes.java`,
  `RUTrunkPlacerTypes.java`, `RUFoliagePlacerTypes.java` + their 20 impl classes under
  `src/common/main/java/net/regions_unexplored/worldgen/` to the codec() pattern.
- Work Unit 2: run the real compiler (not grep/TODO comments — a first draft of this
  plan listed 6 "known" compile errors sourced from TODO comments, and verification
  found 4 of the 6 were already fixed and a 5th was guessed into the wrong files) and
  fix whatever's actually still broken, plus two confirmed-open items
  (`RUDataMapGenerator.java` compostability wiring, `RUPlacedFeatureBootstrap.java`
  tag-conversion TODO — lowest priority).
- Work Unit 3: runtime smoke test both loaders (client + server) since registry/codec
  wiring can compile clean and still blow up at world-load time.

## Constraints

- No AI attribution in commits (user's global CLAUDE.md), conventional commit messages.
- One commit per coherent migration step, matching existing branch style (`82127afd`).
- Branch `26.3-indev` is not the repo's default branch (`21.1` is) — commit directly to
  it, no feature branch needed.
- RDD (receipt-driven development) is ON globally — run `gentle-ai review assess`
  after each work-unit commit per the ODD protocol.

## Tasks

- [x] **T1 — INVALIDATED, no change needed.** A writer applied the codec() migration to
  all 24 files and compiled against the real downloaded MC 26.3 jars (first time the
  local cache had them). It failed hard and conclusively: `BuiltInRegistries.
  TRUNK_PLACER_TYPE`/`ROOT_PLACER_TYPE`/`FOLIAGE_PLACER_TYPE`/`TREE_DECORATOR_TYPE` are
  still typed `Registry<XxxType<?>>`, base classes still declare an abstract `type()`
  hook, and `TreeDecorator` is still a class, not an interface (`interface expected
  here` compiler error). Unlike `Feature`/`BlockStateProvider`, MC 26.2/26.3 did **not**
  collapse these four registries. The existing `TYPE`/`type()` shape in all 24 files is
  already correct — changes were reverted, nothing committed. Two prior source-only
  verification passes wrongly generalized from the confirmed Feature/BlockStateProvider
  case; only a real compile against actual jars caught this.
- [x] **T2 — DONE, project already compiles clean.** Ran all four tasks with zero
  source changes: `compileJava`, `compileFabricJava`, `compileNeoforgeJava`,
  `compileNeoforgeDataJava` all PASS, 0 errors (only 5 pre-existing `[removal]`
  deprecation warnings). `RUCreativeModeTabs.java`/`ParticleRegistry.java`/
  `FallenTreeConfig.java` — flagged in `82127afd`'s commit message as known-remaining
  work — compile fine now too. **There is no outstanding compile-error work on this
  branch.** The "26.3 registry migration" is, compile-wise, already finished.
- [ ] **T3** — Two confirmed-open non-blocking items, neither a compile error:
  - `RUDataMapGenerator.java` (`src/neoforge/data/.../provider/`) — `gather()` (line 20)
    still an empty stub; NeoForge's Compostable data map was removed in favor of the
    vanilla `DataComponents.COMPOSTABLE` set at item-build time. Needs a product decision
    (wire it up at item registration, or confirm it's dead code and remove) before
    touching — not part of this migration's scope unless the user asks for it.
  - `RUPlacedFeatureBootstrap.java:18` — `// TODO: Convert these into tags` on
    `onGrassBlockPredicate`'s hardcoded block list. Cosmetic, pre-existing, not in scope
    unless requested.
  Leaving both as out-of-scope follow-ups unless the user asks to pick them up.
- [x] **T4 — 3/4 done, all PASS so far.** Runtime smoke test (explicitly requested by
  user). No display server existed in this environment; user chose to install Xvfb +
  confirmed Mesa software rendering (llvmpipe) was already present, so client tests run
  headless via `xvfb-run`.
  - `runFabricServer`: PASS — `Done (2.194s)!`, 4 datapacks loaded, 145 biomes, 2403
    advancements, zero registry/codec/ERROR/FATAL log lines.
  - `runNeoforgeServer`: PASS — `Done (3.841s)!`, full mod/datapack bootstrap including
    `regions_unexplored`/`apollib`/`lithostitched`, zero registry/codec errors.
  - `runFabricClient`: PASS, and went further than a menu check — drove the GUI with
    xdotool, created a new world, spawned in, confirmed custom RU biome terrain
    (autumn/snow-peak biome with custom flora) actually generated. This exercises
    trunk/root/foliage placers and tree decorators live, not just at compile time. Zero
    registry/codec errors.
  - `runNeoforgeClient`: pending.
  Environment notes for future runs: `run/` dir must exist and `eula=true` must be set
  before first server boot; this sandbox pre-binds ~436 local ports including
  25500-25599, so boot tests need a randomized `server-port`/`query.port`; a
  gradle-launched server's stdin doesn't reach the forked Minecraft JVM (the Gradle
  Daemon owns it) — stop it by SIGTERM/SIGINT on the actual `KnotServer`/
  `net.neoforged.fml.startup.Server` PID, found via `pstree`/`lsof` on the world's
  `session.lock`.

## Acceptance criteria

- `./gradlew compileJava`, `compileFabricJava`, `compileNeoforgeJava`,
  `compileNeoforgeDataJava` all pass.
- All four `run<Loader><Client|Server>` tasks reach a fully-loaded state with no
  registry/codec exceptions in the log.
- Each task closed with at least one work-unit commit on `26.3-indev`.

## Progress

- Plan validated against live code via Explore agents (2026-10-03): Work Unit 1 claims
  100% confirmed; Work Unit 2 original claims mostly stale (4/6 already fixed, 1 guessed
  into wrong files). Plan file corrected before approval.
- No source changes made yet.

## Next step

Delegate T1 to a writer agent (allowed edit surfaces: the 4 registry files + 20 impl
class files listed above, plus `RUTrunkPlacer.java` read-only reference — confirmed no
change needed there).
